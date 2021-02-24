[![shields](https://img.shields.io/badge/project%20status-validated-deepgreen)](https://shields.io/)
[![shields](https://img.shields.io/badge/made%20with-java-orange)](https://shields.io/)
[![shields](https://img.shields.io/badge/powered%20by-spring-green)](https://shields.io/)
____________________

> Ce README est basé sur les conclusions évoquées dans la présentation réalisée à la fin du projet.

# Concevez une application web Java de A à Z

## Dans le cadre de la formation OpenClassrooms "Développeur d'application Java"

### Objectif du projet
En partant de zéro, développer toutes les couches d'une application Java sécurisée et fonctionnelle.

### Progression
En plus de renforcer mes compétences sur les aspects étudiés au précédent projet, ce projet m'a permis de pratiquer l'intégration d'une base de données dans le circuit de l'application de sa conception jusqu'à la gestion des transactions, et de prendre en main d'autres extensions de Spring, notamment Spring Security.

### Réalisation
En fin de compte, je suis allé au delà de ce qui était demandé, avec plusieurs initiatives :
* Utilisation de librairies spécifiques aux bases de données
  * Flyway pour le versionnage des migrations
  * FlywayDB pour des migrations spécifiques aux tests
  * AssertJ-DB pour les assertions sur les tables et leurs données
* Création d'un module client pour réaliser des tests d'intégration avec RestAssured
* Configuration d'un site Maven pour présenter les rapports
* Description de l'API avec Swagger.io

Le projet poursuit les bonnes pratiques de développement, avec l'isolation et l'indépendance des tests, la séparation des interfaces, l'injection des dépendances, ou encore l'utilisation des Java Pattern (DTO, DAO).

### Axes d'améliorations
* Redéfinition du modèle "Bill" et de sa table associée relativement simpliste
* Valeurs de retour sur les méthodes HTTP POST, PUT, DELETE avec des DTO plutôt qu'une simple chaîne de caractère
* Validation des modèles 

____________________________

# PayApp
## Documentation for PayMyBuddy 'PayApp' Application .

### Build
Build application with command `mvn clean package` or `mvn clean verify`
to include integration tests.

**NOTE :** In that second case, the server-side application will start during build. 
To kill the process, retrieve Spring PID in logs and kill it manually.

_This is in-progress work, a fix will eventually be added._

### Login to application
##### Super user
You can use user with username '**thibaut.beylard@example.com**' and password
'**adminpass**' to connect as a super user with 'ADMIN' authorizations. 

##### Client API
Go to endpoint `/swagger-ui.html` to check all available endpoints for the application.

### Site
Build application including `mvn site:stage` goal to generated Maven sites for whole project. 
Relevant informations will essentially be in payapp-server site where you'll find test reports.

### Appendices

##### UML Diagram

![UML_Diagram](docs/uml.png)

##### Database Model

![UML_Diagram](docs/mpd.png)

NOTE : This Model was generated with _SQL Power Architect_. Due to its limitations, `DATETIME` datatype is represented by `TIMESTAMP`.
