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
- Flyway enabled with schema and seed data for surf spots and ski resorts.
- Swagger/OpenAPI configured.
- Bean Validation enabled for API requests.
- Standard API error responses based on HTTP Problem Details.
- Real current forecasts from Open-Meteo for weather, marine, and ski elevation conditions.
- Surf and ski condition scores calculated from current forecast data.
- Ski resorts modelled with base, middle, and top altitude for elevation-aware forecasts.
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

Flyway creates the initial schema and inserts sample surf spots and ski resorts on application startup.
Migration `V3__add_mid_altitude_to_ski_resorts.sql` adds `mid_altitude` to existing ski resorts and backfills it as the midpoint between base and top altitude.

## External Forecast Provider

Forecast data is provided by Open-Meteo:

```text
https://open-meteo.com/
```

The MVP uses:

- Weather Forecast API for temperature, wind, precipitation, snowfall, snow depth, and freezing level.
- Marine Weather API for wave height, wave period, and swell data.

No API key is required for non-commercial usage. The free tier is suitable for this portfolio project, but Open-Meteo requires attribution and commercial usage requires a commercial plan.

Attribution:

```text
Weather and marine forecast data provided by Open-Meteo (https://open-meteo.com/).
Open-Meteo data is based on open weather data sources and requires attribution under CC BY 4.0 where applicable.
```

If Open-Meteo is unavailable, the API can return deterministic fallback data marked with `dataSource: "MOCK_FALLBACK"`.

## Error Responses

The API returns standardized error responses using HTTP Problem Details.

Example validation error:

```json
{
  "title": "Validation failed",
  "status": 400,
  "detail": "Request validation failed",
  "instance": "/api/surf-spots",
  "errors": {
    "name": "must not be blank"
  }
}
```

Example not found error:

```json
{
  "title": "Resource not found",
  "status": 404,
  "detail": "Surf spot not found with id: 999",
  "instance": "/api/surf-spots/999"
}
```

## API Documentation

When the application is running:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## MVP Endpoints

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

Example ski resort request:

```json
{
  "name": "Baqueira Beret",
  "country": "Spain",
  "region": "Catalonia",
  "latitude": 42.6986,
  "longitude": 0.9347,
  "baseAltitude": 1500,
  "midAltitude": 2055,
  "topAltitude": 2610
}
```

Example surf forecast response:

```json
{
  "spotId": 1,
  "spotName": "Mundaka",
  "dateTime": "2026-09-02T22:15",
  "temperatureC": 19.0,
  "windSpeedKmh": 1.4,
  "windDirectionDegrees": 180,
  "windDirectionCompass": "S",
  "precipitationMm": 0.0,
  "waveHeightM": 0.6,
  "wavePeriodS": 6.3,
  "swellWaveHeightM": 0.52,
  "swellWavePeriodS": 5.8,
  "swellWaveDirectionDegrees": 315,
  "swellWaveDirectionCompass": "NW",
  "dataSource": "OPEN_METEO"
}
```

Example ski forecast response shape:

```json
{
  "resortId": 1,
  "resortName": "Baqueira Beret",
  "base": { "elevationType": "BASE", "elevation": 1500 },
  "mid": { "elevationType": "MID", "elevation": 2055 },
  "top": { "elevationType": "TOP", "elevation": 2610 },
  "dataSource": "OPEN_METEO"
}
```

## Roadmap

- Implement CRUD for surf spots.
- Implement CRUD for ski resorts.
- Add global API error handling.
- Add seed data with Flyway.
- Add real Open-Meteo forecasts with deterministic fallback.
- Add surf and ski scoring.
- Add unit tests for services and scoring.
- Add integration tests, CI/CD, auth, and deployment after the MVP is complete.

## Technical Decisions

- Maven is used for a conventional Spring Boot setup.
- Flyway owns database schema creation from the beginning.
- Hibernate `ddl-auto` is set to `validate` to avoid accidental schema drift.
- PostgreSQL runs locally through Docker Compose.
- Authentication is deferred to keep the first MVP focused on API fundamentals.
