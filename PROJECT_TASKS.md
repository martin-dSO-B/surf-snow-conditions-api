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

- [x] Test unitario de `SurfSpotService`.
- [x] Test unitario de `SkiResortService`.
- [x] Test unitario de `SurfScoringService`.
- [x] Test unitario de `SkiScoringService`.
- [x] Test básico de contexto Spring.
- [x] Documentar cómo ejecutar tests.

## Fase 6 - Forecast Futuro Y Granularidad

- [ ] Definir DTOs de forecast futuro para surf con lista temporal de puntos horarios.
- [ ] Definir DTOs de forecast futuro para ski con lista temporal de puntos horarios por cota: base, media y alta.
- [ ] Ampliar integración Open-Meteo Weather API usando `hourly` en lugar de depender solo de `current`.
- [ ] Ampliar integración Open-Meteo Marine API usando `hourly` para oleaje, periodo y swell.
- [ ] Limitar el horizonte inicial a 48 horas para mantener respuestas útiles y ligeras.
- [ ] Usar granularidad horaria inicial: 1 salida por hora.
- [ ] Crear endpoint agregado de condiciones de surf que devuelva spot, forecast horario, score horario y mejor ventana en una sola llamada Front -> Back.
- [ ] Crear endpoint agregado de condiciones de ski que devuelva resort, forecast horario por cotas, score horario y mejor ventana en una sola llamada Front -> Back.
- [ ] Mantener los endpoints actuales de forecast/score como vista de condiciones actuales o compatibilidad interna mientras aporte valor.
- [ ] Evitar llamadas duplicadas a Open-Meteo reutilizando datos de forecast para calcular scores.
- [ ] Añadir cache backend simple con TTL corto para forecast por localización y horizonte, sin Redis en esta fase.
- [ ] Añadir fallback mockeado también para forecast futuro.
- [ ] Añadir tests unitarios para mapeo temporal, cálculo de score horario y selección de mejor ventana.
- [ ] Documentar en README el horizonte, granularidad y estrategia de eficiencia.

## Fase 7 - App Web Visual

- [ ] Crear aplicación frontend con React, TypeScript y Vite.
- [ ] Definir estructura `frontend/` dentro del repositorio.
- [ ] Crear cliente API para consumir los endpoints agregados del backend Spring Boot.
- [ ] Crear pantalla principal con resumen de surf spots y ski resorts.
- [ ] Crear vista de detalle de surf spot con forecast horario, surf score horario y mejor ventana.
- [ ] Crear vista de detalle de ski resort con forecast horario, ski score horario y mejor ventana.
- [ ] Mostrar scores con tarjetas, colores y recomendaciones visibles.
- [ ] Añadir estados de carga, error y datos vacíos.
- [ ] Configurar CORS en Spring Boot para desarrollo local.
- [ ] Documentar en README cómo ejecutar backend y frontend.

## Fase 8 - Calidad, CI Y Docker

- [ ] Añadir Testcontainers.
- [ ] Añadir GitHub Actions.
- [ ] Añadir Dockerfile para backend.
- [ ] Añadir Dockerfile para frontend si aporta valor.
- [ ] Preparar ejecución conjunta con Docker Compose si aporta valor.

## Fase 9 - Auth, Favoritos Y Alertas

- [ ] Añadir Spring Security.
- [ ] Añadir registro/login.
- [ ] Añadir JWT.
- [ ] Añadir favoritos.
- [ ] Añadir alertas.

## Fase 10 - Despliegue Completo

- [ ] Desplegar API online.
- [ ] Desplegar app web online.
- [ ] Configurar PostgreSQL gestionado.
- [ ] Configurar variables de entorno.
- [ ] Añadir URLs de demo al README.
