# Projet Intergiciel - FISA 4 INFO - INSA Hdf
[![forthebadge](http://forthebadge.com/images/badges/built-with-love.svg)](https://github.com/mayel15/projet-intergiciel) [![forthebadge](https://forthebadge.com/images/badges/made-with-java.png)](https://github.com/mayel15/projet-intergiciel)

| ![Alaaeddin ALMAJJO](https://avatars.githubusercontent.com/u/77294802?v=4)  | ![Ayman DOULKOM](https://avatars.githubusercontent.com/u/116734751?v=4)          | ![Pape THIAM](https://avatars.githubusercontent.com/u/97792012?v=4) |
| :--------------: | :--------------: | :--------------: |
| Alaaeddin ALMAJJO | Ayman DOULKOM        | Pape THIAM  |
| [@aladinMJ](https://github.com/aladinMJ) | [@ayman-h226](https://github.com/ayman-h226)        | [@mayel15](https://github.com/mayel15)  |
| alaaeddin.almajjo@uphf.fr  | ayman.doulkom@uphf.fr           | papemayeldiagne.thiam@uphf.fr  |
| 22001993  | 00000000           | 22009010  |

## Technos utilisées

<a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="100" height="100"/> </a> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://imgs.search.brave.com/vUNX5vHj053oH8GdZXva9X8byPP-0OQMCLXSgv3rLtU/rs:fit:500:0:0/g:ce/aHR0cHM6Ly9zdGF0/aWMud2lraWEubm9j/b29raWUubmV0L2xv/Z29wZWRpYS9pbWFn/ZXMvZC9kOC9BcGFj/aGVfS2Fma2FfTG9n/by5qcGcvcmV2aXNp/b24vbGF0ZXN0L3Nj/YWxlLXRvLXdpZHRo/LWRvd24vMzAwP2Ni/PTIwMjIwNzAzMDIz/NjEz.jpeg" alt="java" width="260" height="100"/> </a> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://imgs.search.brave.com/IJhQoF6ymM5JPT7_Jn6SHWO8dj8NggPgkQE1gYJ72zo/rs:fit:500:0:0/g:ce/aHR0cHM6Ly93d3cu/bWVkaXRlY3MuY29t/L3dwLWNvbnRlbnQv/dXBsb2Fkcy9taXJ0/aC1sb2dvLndlYnA" alt="java" width="175" height="100"/> </a>

## Description du projet 

Ce projet consiste à développer différentes applications, utilisant les API Kafka-Client et/ou le Framework SpringBoot, pour la manipulation de fichiers HL7 de type ADT. L'objectif est de lire, parser et formater ces fichiers en format JSON, puis de les transporter via un topic Kafka vers une base de données PostgreSQL.

![alt text](synoptique-echanges.png)

L'architecture comprend plusieurs modules, notamment des producteurs et des consommateurs Kafka, ainsi qu'une application console permettant de requêter la base de données. Les échanges se font via des flux d'événements Kafka.

Les principales fonctionnalités incluent:
- la lecture des fichiers HL7, leur conversion en JSON
- leur envoi via Kafka, l'intégration dans la base de données
- la possibilité de requêter la base de données via une console texte des commandes prédéfinies.

Les livrables comprennent :
- le code source des différents modules
- le paramétrage du service Kafka
- le docker-compose `docker-compose.yml` adapté
- le script sh `create-topics` pour la creation des topics Kafka 
- le script SQL `database-creation.sql` pour la création de la base de données
- le `rapport` de projet répondant aux questions posées
- et ce `README.md` contenant les informations sur le projet et l'équipe de développement
