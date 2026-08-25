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

- [ ] Añadir Bean Validation.
- [ ] Añadir excepción `ResourceNotFoundException`.
- [ ] Añadir `GlobalExceptionHandler`.
- [ ] Añadir formato estándar de error.
- [x] Añadir Swagger/OpenAPI.
- [ ] Añadir datos iniciales con Flyway.

## Fase 4 - Forecast Y Scoring

- [ ] Crear módulo `forecast`.
- [ ] Crear forecast mockeado para surf.
- [ ] Crear forecast mockeado para ski.
- [ ] Crear módulo `scoring`.
- [ ] Implementar surf score.
- [ ] Implementar ski score.
- [ ] Añadir endpoints `/api/scores/surf/{spotId}`.
- [ ] Añadir endpoints `/api/scores/ski/{resortId}`.

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
