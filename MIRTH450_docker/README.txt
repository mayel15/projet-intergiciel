Compiler le container docker mith450
====================================

Se rendre dasn le dossier ./DockerHub

lancer la commande build.cmd ou build.sh selon votre système
ceci va générer sur votre machine locale une image docker qui se nomme 
mirth450

Lancer le docker compose mirth450
=================================
docker-compose up -d -f docker-compose_mirth450.yml

Utilisation
===========

Ce docker compose permet de démarrer mirth connect et une base de données postgresql version 16, la bd est stocké en local sur votre machine, elle est accessible via le port 5435 en externe.

Mirth connect est disponible sur le port 8444

https://localhost:8444

vous devez utiliser un client Administrator launcher a installé impérativement sur votre pc pour y acceder.

https://www.nextgen.com/solutions/interoperability/mirth-integration-engine/mirth-connect-downloads


 
