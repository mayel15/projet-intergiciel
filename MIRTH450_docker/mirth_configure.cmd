set IP=%1


REM escape les $ => \$
REM supprimer les LF et convertir en espace
REM supprimer 2 espaces de manière récurente => garde uniquement les espaces impair en unique espace

REM Lecture du fichier backup mirth connect puis conversion pour curl
REM serverConf=$(tr '\n' ' ' < ./Mirth_Backup.xml | sed 's/  //g' | sed 's/\$/\\\$/g')
REM serverConf=$(cat ./Mirth_Backup.xml)

REM se connecter en temp que admin/admin
REM ecrire le cookies temporairement sur disque
curl --insecure --cookie-jar ./mirthcook -X POST "https://%IP%:8444/api/users/_login" -H "accept: application/xml" -H "Content-Type: application/x-www-form-urlencoded" -d "username=admin&password=admin"

REM appliquer le paramètrage de l'utilisateur...
curl --cookie ./mirthcook --insecure -X PUT "https://%IP%:8444/api/users/1" -H "accept: application/xml" -H "Content-Type: application/xml" -d "<user> <id>1</id> <username>admin</username> <email>admin@calyps.com</email> <firstName>Calyps</firstName> <lastName>Saniia</lastName> <organization>Calyps</organization> <description></description> <phoneNumber></phoneNumber> <industry>HIT Software</industry> <lastLogin> <time>1628494580581</time> <timezone>Etc/UTC</timezone> </lastLogin> <gracePeriodStart> <time>1628494580581</time> <timezone>Etc/UTC</timezone> </gracePeriodStart> <strikeCount>1</strikeCount> <lastStrikeTime> <time>1628494580581</time> <timezone>Etc/UTC</timezone> </lastStrikeTime></user>"

REM appliquer les préférences utilisateurs
curl --cookie ./mirthcook --insecure -X PUT "https://%IP%:8444/api/users/1/preferences" -H "accept: application/xml" -H "Content-Type: application/xml" -d "<properties><property name=\"checkForNotifications\">false</property><property name=\"firstlogin\">false</property><property name=\"showNotificationPopup\">false</property></properties>"

REM importer les channels

curl --cookie ./mirthcook --insecure -X PUT "https://%IP%:8444/api/server/configuration?deploy=true&overwriteConfigMap=true" -H "accept: application/xml" -H "Content-Type: application/xml" -d @Mirth_Backup.xml

REM redeploiement des channels => existe déja avec l'option true sur la setup
curl --cookie ./mirthcook --insecure -X POST "https://%IP%:8444/api/channels/_redeployAll?returnErrors=true" -H "accept: application/xml"


REM charger le mot de passe par défaut et se deloguer
curl --cookie ./mirthcook --insecure -X PUT "https://%IP%:8444/api/users/1/password" -H "accept: application/xml" -H "Content-Type: text/plain" -d "69fda3"
curl --cookie ./mirthcook --insecure -X POST "https://%IP%:8444/api/users/_logout" -H "accept: application/xml"

REM supprimer le cookies
del mirthcook

