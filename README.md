# Surf & Snow Conditions API

Backend portfolio project built with Java 21 and Spring Boot to manage surf spots, ski resorts, mocked forecasts, and basic condition scores.

## Goal

This project is designed as a professional portfolio API for demonstrating modern backend development: REST API design, layered architecture, PostgreSQL, Flyway, Docker, OpenAPI documentation, and automated tests.

## Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Flyway
- Docker Compose
- OpenAPI/Swagger with springdoc-openapi
- JUnit 5, Mockito, AssertJ through Spring Boot Test

## Current Status

Base project initialized:

- Maven Spring Boot project.
- Base package `com.portfolio.surfsnow`.
- Clean package structure for `spot`, `resort`, `forecast`, `scoring`, and `shared`.
- PostgreSQL configured through Docker Compose.
- Flyway enabled with an initial schema for surf spots and ski resorts.
- Swagger/OpenAPI configured.
- Basic Spring context test.

Authentication is intentionally not included in the MVP base.

## Architecture

Recommended package structure:

```text
src/main/java/com/portfolio/surfsnow
├── spot
├── resort
├── forecast
├── scoring
└── shared
    ├── config
    ├── exception
    └── validation
```

The API will follow a simple layered design:

```text
Controller -> Service -> Repository -> Entity
DTOs for API requests/responses
Mappers kept simple and local unless reuse justifies extraction
Global exception handling under shared.exception
```

## Local Setup

Requirements:

- Java 21
- Maven 3.9+
- Docker and Docker Compose

Start PostgreSQL:

```bash
docker compose up -d
```

Run the application:

```bash
mvn spring-boot:run
```

Run tests:

```bash
mvn test
```

## Database

Default local database settings:

```text
POSTGRES_DB=surfsnow
POSTGRES_USER=surfsnow
POSTGRES_PASSWORD=surfsnow
```

The application uses these defaults:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/surfsnow
SPRING_DATASOURCE_USERNAME=surfsnow
SPRING_DATASOURCE_PASSWORD=surfsnow
```

Flyway migrations live in:

```text
src/main/resources/db/migration
```

## API Documentation

When the application is running:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Planned MVP Endpoints

Surf spots:

```text
GET    /api/surf-spots
POST   /api/surf-spots
GET    /api/surf-spots/{id}
PUT    /api/surf-spots/{id}
DELETE /api/surf-spots/{id}
```

Ski resorts:

```text
GET    /api/ski-resorts
POST   /api/ski-resorts
GET    /api/ski-resorts/{id}
PUT    /api/ski-resorts/{id}
DELETE /api/ski-resorts/{id}
```

Forecasts:

```text
GET /api/forecasts/surf/{spotId}
GET /api/forecasts/ski/{resortId}
```

Scores:

```text
GET /api/scores/surf/{spotId}
GET /api/scores/ski/{resortId}
```

## Roadmap

- Implement CRUD for surf spots.
- Implement CRUD for ski resorts.
- Add global API error handling.
- Add seed data with Flyway.
- Add mocked forecasts.
- Add surf and ski scoring.
- Add unit tests for services and scoring.
- Add integration tests, CI/CD, auth, and deployment after the MVP is complete.

## Technical Decisions

- Maven is used for a conventional Spring Boot setup.
- Flyway owns database schema creation from the beginning.
- Hibernate `ddl-auto` is set to `validate` to avoid accidental schema drift.
- PostgreSQL runs locally through Docker Compose.
- Authentication is deferred to keep the first MVP focused on API fundamentals.
