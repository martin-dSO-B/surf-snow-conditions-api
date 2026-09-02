# Project Tasks - Surf & Snow Conditions API

## Fase 1 - Base Del Proyecto

- [x] Crear proyecto Spring Boot con Java 21.
- [x] Configurar dependencias: Web, Data JPA, Validation, PostgreSQL, Flyway, OpenAPI, Test.
- [x] Crear package base `com.portfolio.surfsnow`.
- [x] Crear `docker-compose.yml` con PostgreSQL.
- [x] Configurar `application.yml`.
- [x] Crear migración inicial de Flyway.
- [x] Crear README inicial.
- [x] Crear el repositorio con la base inicial del mismo.

## Fase 2 - CRUD MVP

- [x] Implementar entidad `SurfSpot`.
- [x] Implementar DTOs de `SurfSpot`.
- [x] Implementar repository de `SurfSpot`.
- [x] Implementar service de `SurfSpot`.
- [x] Implementar controller de `SurfSpot`.
- [x] Implementar entidad `SkiResort`.
- [x] Implementar DTOs de `SkiResort`.
- [x] Implementar repository de `SkiResort`.
- [x] Implementar service de `SkiResort`.
- [x] Implementar controller de `SkiResort`.

## Fase 3 - Calidad API

- [x] Añadir Bean Validation.
- [x] Añadir excepción `ResourceNotFoundException`.
- [x] Añadir `GlobalExceptionHandler`.
- [x] Añadir formato estándar de error.
- [x] Añadir Swagger/OpenAPI.
- [x] Añadir datos iniciales con Flyway.

## Fase 4 - Forecast Y Scoring

- [x] Paso previo: añadir `midAltitude` a ski resorts.
- [x] Crear migración Flyway `V3__add_mid_altitude_to_ski_resorts.sql`.
- [x] Actualizar entidad `SkiResort` con `midAltitude`.
- [x] Actualizar DTOs CRUD de `SkiResort` con `midAltitude`.
- [x] Actualizar validación `baseAltitude <= midAltitude <= topAltitude`.
- [x] Crear módulo `forecast`.
- [x] Integrar Open-Meteo Weather API.
- [x] Integrar Open-Meteo Marine API.
- [x] Crear forecast real para surf.
- [x] Crear forecast real para ski con base, cota intermedia y cota alta.
- [x] Crear fallback mockeado para forecasts.
- [x] Añadir endpoint `/api/forecasts/surf/{spotId}`.
- [x] Añadir endpoint `/api/forecasts/ski/{resortId}`.
- [x] Crear módulo `scoring`.
- [x] Implementar surf score.
- [x] Implementar ski score.
- [x] Añadir endpoints `/api/scores/surf/{spotId}`.
- [x] Añadir endpoints `/api/scores/ski/{resortId}`.
- [x] Documentar Open-Meteo y atribución en README.

## Fase 5 - Tests

- [ ] Test unitario de `SurfSpotService`.
- [ ] Test unitario de `SkiResortService`.
- [ ] Test unitario de `SurfScoringService`.
- [ ] Test unitario de `SkiScoringService`.
- [x] Test básico de contexto Spring.
- [x] Documentar cómo ejecutar tests.

## Fase 6 - Avanzado

- [ ] Añadir Spring Security.
- [ ] Añadir registro/login.
- [ ] Añadir JWT.
- [ ] Añadir favoritos.
- [ ] Añadir alertas.
- [ ] Añadir Testcontainers.
- [ ] Añadir GitHub Actions.
- [ ] Añadir Dockerfile.
- [ ] Desplegar online.
