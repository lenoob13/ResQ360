# Architecture de l'Application

## Introduction
Cette application permet la gestion des secouristes et des dispositifs prévisionnels de secours (DPS). Elle offre des fonctionnalités pour les administrateurs et les secouristes afin de faciliter l'organisation des interventions.

## Architecture Générale
L'application suit une architecture en couches et repose sur les technologies suivantes :
- **JavaFX** pour l'interface utilisateur
- **MySQL** pour la base de données
- **JDBC** pour la communication avec la base de données
- **DAO (Data Access Object)** pour la gestion des accès aux données
- **MVC (Modèle-Vue-Contrôleur)** pour la structuration de l'application

## Cas d'Utilisation
L'application offre plusieurs fonctionnalités :
- **Pour les Secouristes** :
    - Visualiser leur planning
    - Mettre à jour leurs disponibilités
    - Mettre à jour leurs caractéristiques et compétences
- **Pour les Administrateurs** :
    - Visualiser les plannings
    - Gérer les DPS
    - Gérer les secouristes
    - Affecter les secouristes aux missions

## Structure de l'Application
L'architecture de l'application est divisée en plusieurs couches :

### 1. Vue (JavaFX)
- Structure l'interface utilisateur
- Gère les interactions de l'utilisateur

### 2. Contrôleur (JavaFX)
- Gère les actions de l'utilisateur
- Communique avec la couche métier

### 3. Métier
- Contient la logique de gestion (services)
- Gère la persistance des données
- Implémente des algorithmes de gestion des plannings et des affectations

### 4. DAO (Data Access Object)
- Gère les opérations CRUD (Create, Read, Update, Delete) avec la base de données
- Utilise JDBC pour interagir avec MySQL

## Modèle de Données (Base de Données)
L'application repose sur un modèle relationnel comprenant plusieurs entités principales :
- **Secouriste** (id, nom, prénom, date de naissance, email, téléphone, adresse)
- **DPS** (id, horaires, site d'intervention)
- **Journée** (date, mois, année)
- **Compétence** (intitulé)
- **Sport** (code, nom)
- **Site** (code, nom, coordonnées GPS)
- **Besoin** (nombre de secouristes requis)

## Flux de Données
1. Un administrateur gère les plannings et affecte les secouristes aux DPS.
2. Les secouristes mettent à jour leurs disponibilités.
3. L'application stocke ces informations en base de données.
4. Les administrateurs et secouristes peuvent consulter les affectations et plannings via l'interface JavaFX.

## Conclusion
Cette architecture modulaire permet une séparation claire des responsabilités, facilitant la maintenance et l'évolution de l'application. Le modèle relationnel garantit une gestion efficace des données et l'utilisation de JavaFX assure une interface utilisateur fluide et réactive.

