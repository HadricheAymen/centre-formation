# Application de Gestion d'un Centre de Formation

## Description

Application web Spring Boot pour la gestion d'un centre de formation permettant :
- Aux **administrateurs** de gérer les étudiants, formateurs, cours et inscriptions
- Aux **étudiants** de consulter leurs cours, notes et de s'inscrire en ligne
- Aux **formateurs** de gérer les cours qu'ils dispensent et les notes

## Technologies Utilisées

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Persistance des données
- **Spring Security** - Authentification et autorisation
- **Thymeleaf** - Rendu côté serveur (SSR)
- **H2 Database** - Base de données en mémoire (développement)
- **MySQL** - Base de données (production)
- **Maven** - Gestion des dépendances
- **SpringDoc OpenAPI** - Documentation API REST

## Architecture

L'application suit une architecture en couches :

```
src/main/java/spring/jpa/centre_formation/
├── entity/              # Entités JPA
├── repository/          # Repositories Spring Data JPA
├── service/             # Couche métier
├── controller/
│   ├── rest/           # API REST (CSR)
│   └── web/            # Contrôleurs Thymeleaf (SSR)
├── security/           # Configuration de sécurité
├── config/             # Configurations Spring
└── exception/          # Exceptions personnalisées
```

## Modèle de Données

### Entités Principales

1. **User** (classe abstraite)
   - Etudiant
   - Formateur

2. **Cours**
   - Code, titre, description
   - Nombre d'heures, coefficient
   - Relations : Formateur, Spécialité, Session, Groupes

3. **Inscription**
   - Relation Etudiant-Cours
   - Date d'inscription, statut actif

4. **Note**
   - Valeur (0-20), type d'évaluation
   - Date, commentaire
   - Relations : Etudiant, Cours

5. **Seance**
   - Date début/fin, salle, type
   - Gestion des conflits (salle, formateur)

6. **Groupe**
   - Code, nom, capacité maximale

7. **Specialite**
   - Code, nom, description

8. **SessionPedagogique**
   - Code, nom, dates, statut actif

## Fonctionnalités

### Gestion des Utilisateurs
- Création, modification, suppression d'étudiants et formateurs
- Authentification avec Spring Security
- Gestion des rôles (ADMIN, FORMATEUR, ETUDIANT)
- Encodage des mots de passe avec BCrypt

### Gestion des Cours
- CRUD complet des cours
- Affectation de formateurs, spécialités, sessions, groupes
- Calcul de statistiques (moyenne, taux de réussite)
- Recherche et filtrage

### Gestion des Inscriptions
- Inscription des étudiants aux cours
- Vérification des doublons
- Annulation d'inscriptions
- Consultation des inscriptions actives

### Gestion des Notes
- Saisie et modification des notes
- Calcul de moyennes (par étudiant, par cours)
- Validation des notes (0-20)
- Vérification de l'inscription avant notation

### Gestion des Séances
- Planification des séances
- Détection des conflits (salle, formateur)
- Emploi du temps pour formateurs et étudiants
- Annulation de séances

## API REST

L'application expose des API REST pour toutes les fonctionnalités :

### Endpoints Principaux

- **Etudiants** : `/api/etudiants`
- **Formateurs** : `/api/formateurs`
- **Cours** : `/api/cours`
- **Inscriptions** : `/api/inscriptions`
- **Notes** : `/api/notes`
- **Séances** : `/api/seances`
- **Groupes** : `/api/groupes`
- **Spécialités** : `/api/specialites`
- **Sessions** : `/api/sessions`

### Documentation API

La documentation interactive de l'API est disponible via Swagger UI :
- URL : `http://localhost:8080/swagger-ui.html`

## Configuration

### Base de Données H2 (Développement)

Par défaut, l'application utilise H2 en mémoire :

```properties
spring.datasource.url=jdbc:h2:mem:formationdb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Console H2 : `http://localhost:8080/h2-console`

### Base de Données MySQL (Production)

Pour utiliser MySQL, décommenter dans `application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/centre_formation
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

## Installation et Exécution

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+ (pour la production)

### Étapes

1. **Cloner le projet**
```bash
git clone <repository-url>
cd centre-formation
```

2. **Compiler le projet**
```bash
mvn clean install
```

3. **Exécuter l'application**
```bash
mvn spring-boot:run
```

4. **Accéder à l'application**
- Application : `http://localhost:8080`
- Console H2 : `http://localhost:8080/h2-console`
- API Swagger : `http://localhost:8080/swagger-ui.html`

## Sécurité

### Rôles et Permissions

- **ADMIN** : Accès complet à toutes les fonctionnalités
- **FORMATEUR** : Gestion des cours, notes, séances
- **ETUDIANT** : Consultation des cours, notes, inscriptions

### Endpoints Sécurisés

Les endpoints sont sécurisés avec `@PreAuthorize` :

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Etudiant> createEtudiant(...)

@PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
public ResponseEntity<List<Note>> getAllNotes(...)
```

## Tests

Exécuter les tests :

```bash
mvn test
```

## Structure du Projet

```
centre-formation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── spring/jpa/centre_formation/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── controller/
│   │   │       ├── security/
│   │   │       ├── config/
│   │   │       └── exception/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/
│   │       └── static/
│   └── test/
├── pom.xml
└── README.md
```

## Auteur

Projet réalisé dans le cadre du cours "Architectures Logicielles Évoluées : Framework Spring"

## Licence

Ce projet est à usage éducatif.

