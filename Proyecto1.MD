# Surf & Snow Conditions API - Contexto Para Opencode

## Objetivo Del Proyecto

Construir una API backend moderna en Java y Spring Boot para consultar, guardar y valorar condiciones meteorológicas de spots de surf y estaciones de ski.

Este proyecto forma parte de un portfolio profesional orientado a demostrar capacidad real en backend moderno, APIs REST, PostgreSQL, testing, Docker, CI/CD, documentación técnica y despliegue online.

El objetivo no es hacer una demo superficial, sino un proyecto presentable en GitHub, LinkedIn y entrevistas técnicas para roles como Java Backend Developer, Backend Engineer, Spring Boot Developer o Backend/Data Developer.

## Nombre Del Proyecto

Nombre funcional:

```text
Surf & Snow Conditions API
```

Nombre recomendado del repositorio:

```text
surf-snow-conditions-api
```

Package base recomendado:

```text
com.portfolio.surfsnow
```

## Perfil Profesional Que Debe Reforzar

Este proyecto debe ayudar a posicionarme como:

```text
Software Engineer | Java, SQL, Spring Boot | Backend & Data Engineering | MSc Data Science
```

Debe compensar el gap de mi experiencia laboral actual, centrada en Java legacy, JSP, Oracle y PL/SQL, demostrando experiencia práctica con stack moderno.

El proyecto debe evidenciar:

- Java moderno.
- Spring Boot moderno.
- Diseño de API REST.
- Arquitectura por capas.
- PostgreSQL.
- JPA/Hibernate.
- Flyway.
- Testing moderno.
- Docker.
- GitHub Actions.
- OpenAPI/Swagger.
- Seguridad con JWT en fase avanzada.
- Despliegue online.
- Documentación profesional.

## Stack Técnico Principal

Backend:

```text
Java 21
Spring Boot 3.x
Spring Web
Spring Data JPA
Spring Security
Bean Validation
OpenAPI / Swagger
```

Base de datos:

```text
PostgreSQL
Flyway
SQL
JPA/Hibernate
```

Testing:

```text
JUnit 5
Mockito
AssertJ
Spring Boot Test
Testcontainers
```

DevOps:

```text
Docker
Docker Compose
GitHub Actions
Render, Railway o Fly.io
```

Calidad:

```text
Conventional Commits
README profesional
Arquitectura documentada
Issues o GitHub Projects
```

Lombok y MapStruct son opcionales. Si usarlos añade más complejidad que valor, priorizar código explícito y simple.

## Principios De Implementación

Priorizar una solución limpia, realista y mantenible antes que una arquitectura sobredimensionada.

Aplicar estos criterios:

- Empezar por un MVP sólido.
- Evitar sobreingeniería.
- Mantener una arquitectura por capas clara.
- Separar controladores, servicios, repositorios, DTOs, entidades y excepciones.
- Usar validaciones con Bean Validation.
- Usar migraciones de base de datos desde el inicio con Flyway.
- Documentar endpoints con OpenAPI.
- Escribir tests unitarios para lógica de negocio.
- Añadir tests de integración cuando el MVP esté estable.
- Mantener commits pequeños y descriptivos.
- Priorizar código entendible para entrevistas técnicas.

## Arquitectura Recomendada

Estructura de paquetes:

```text
src/main/java/com/portfolio/surfsnow
├── auth
├── user
├── spot
├── resort
├── forecast
├── scoring
├── favorite
├── alert
└── shared
    ├── config
    ├── exception
    └── validation
```

Capas recomendadas:

```text
Controller
Service
Repository
Entity / Domain Model
DTO
Mapper
Exception Handler
Configuration
```

Para el MVP inicial, no crear abstracciones innecesarias. Usar interfaces solo cuando aporten valor real o faciliten testing.

## Funcionalidades Del MVP

El MVP debe incluir:

- CRUD de spots de surf.
- CRUD de estaciones de ski.
- Consulta de forecast mockeado.
- Cálculo de score básico de surf.
- Cálculo de score básico de ski.
- API REST documentada con Swagger/OpenAPI.
- PostgreSQL como base de datos.
- Flyway para migraciones.
- Docker Compose para levantar PostgreSQL.
- Tests unitarios de servicios.
- README completo con instrucciones de ejecución.

No incluir autenticación en el primer MVP salvo que ya esté cerrada la parte CRUD, scoring, tests y documentación.

## Funcionalidades Avanzadas

Después del MVP, añadir:

- Registro de usuarios.
- Login.
- Autenticación JWT.
- Roles de usuario.
- Favoritos.
- Alertas por buenas condiciones.
- Histórico de forecasts.
- Tests de integración con Testcontainers.
- GitHub Actions.
- Dockerfile de la aplicación.
- Despliegue online.
- Integración con API meteorológica real.
- Cache con Redis solo si aporta valor claro.

## Entidades Principales

### User

Campos:

```text
id
email
passwordHash
name
role
createdAt
```

Debe reservarse para la fase de autenticación avanzada.

### SurfSpot

Campos:

```text
id
name
country
region
latitude
longitude
difficulty
bestWindDirection
bestSwellDirection
createdAt
```

Notas:

- `difficulty` puede ser `BEGINNER`, `INTERMEDIATE`, `ADVANCED` o `EXPERT`.
- `bestWindDirection` y `bestSwellDirection` pueden modelarse inicialmente como texto o enum.
- Validar nombre, país, región, latitud y longitud.

### SkiResort

Campos:

```text
id
name
country
region
latitude
longitude
baseAltitude
topAltitude
createdAt
```

Notas:

- Validar altitudes positivas.
- `topAltitude` debe ser mayor o igual que `baseAltitude`.
- Validar latitud y longitud.

### Forecast

Campos:

```text
id
locationType
locationId
date
temperatureC
windSpeedKmh
windDirection
precipitationMm
waveHeightM
wavePeriodS
snowProbability
createdAt
```

Notas:

- `locationType` puede ser `SURF_SPOT` o `SKI_RESORT`.
- Para forecasts de surf se usan campos de ola.
- Para forecasts de ski se usan campos de nieve.
- En el MVP puede generarse forecast mockeado sin persistencia obligatoria.
- Más adelante se puede persistir histórico.

### ConditionScore

Campos:

```text
id
forecastId
activityType
score
recommendation
createdAt
```

Notas:

- `activityType` puede ser `SURF` o `SKI`.
- `score` debe estar entre 0 y 10.
- La recomendación debe ser textual y útil.

### FavoriteLocation

Campos:

```text
id
userId
locationType
locationId
createdAt
```

Reservado para fase avanzada.

### AlertRule

Campos:

```text
id
userId
locationType
locationId
activityType
minimumScore
active
createdAt
```

Reservado para fase avanzada.

## Endpoints Del MVP

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

Documentación:

```text
GET /swagger-ui/index.html
GET /v3/api-docs
```

## Endpoints Avanzados

Auth:

```text
POST /api/auth/register
POST /api/auth/login
```

Favoritos:

```text
GET    /api/users/me/favorites
POST   /api/users/me/favorites
DELETE /api/users/me/favorites/{id}
```

Alertas:

```text
GET    /api/users/me/alerts
POST   /api/users/me/alerts
PUT    /api/users/me/alerts/{id}
DELETE /api/users/me/alerts/{id}
```

## Reglas Iniciales De Scoring

### Surf Score

Variables:

```text
waveHeightM
wavePeriodS
windSpeedKmh
windDirection
temperatureC
precipitationMm
```

Regla inicial:

```text
score = waveScore + periodScore + windScore + weatherScore
```

El resultado final debe normalizarse entre 0 y 10.

Criterios orientativos:

- Olas demasiado pequeñas penalizan.
- Olas moderadas favorecen nivel principiante/intermedio.
- Periodo alto mejora el score.
- Viento fuerte penaliza.
- Precipitación fuerte penaliza.
- Temperatura muy baja penaliza ligeramente.
- Viento offshore o suave debería mejorar el score si se modela dirección.

Ejemplo de respuesta:

```json
{
  "spot": "Somo",
  "date": "2027-01-18",
  "waveHeightM": 1.4,
  "wavePeriodS": 11,
  "windSpeedKmh": 12,
  "windDirection": "S",
  "temperatureC": 13,
  "surfScore": 8.1,
  "recommendation": "Good conditions for intermediate surfers"
}
```

### Ski Score

Variables:

```text
temperatureC
snowProbability
windSpeedKmh
precipitationMm
baseAltitude
topAltitude
```

Regla inicial:

```text
score = snowScore + temperatureScore + windScore + visibilityScore
```

El resultado final debe normalizarse entre 0 y 10.

Criterios orientativos:

- Temperaturas bajo cero o cercanas a cero favorecen.
- Alta probabilidad de nieve mejora el score.
- Viento fuerte penaliza.
- Estaciones con mayor altitud pueden tener bonus leve.
- Precipitación líquida con temperatura positiva penaliza.
- Condiciones extremas deben penalizar aunque haya nieve.

Ejemplo de respuesta:

```json
{
  "resort": "Alto Campoo",
  "date": "2027-02-04",
  "temperatureC": -2,
  "windSpeedKmh": 18,
  "snowProbability": 0.72,
  "skiScore": 7.4,
  "recommendation": "Good conditions, check wind before travelling"
}
```

## Datos Iniciales Recomendados

Surf spots iniciales:

```text
Somo
Loredo
Liencres
Zarautz
Sopelana
Mundaka
```

Ski resorts iniciales:

```text
Alto Campoo
Baqueira Beret
Formigal
Astún
Candanchú
Valdezcaray
```

Estos datos pueden cargarse mediante Flyway en una migración `V2__insert_initial_locations.sql`.

## Migraciones Flyway Recomendadas

Migraciones iniciales:

```text
V1__create_initial_schema.sql
V2__insert_initial_locations.sql
```

Más adelante:

```text
V3__create_users_table.sql
V4__create_favorites_table.sql
V5__create_alert_rules_table.sql
V6__create_forecast_history_table.sql
```

## Manejo De Errores

Crear un manejador global de excepciones en:

```text
com.portfolio.surfsnow.shared.exception
```

Debe cubrir:

- Recurso no encontrado.
- Validación fallida.
- Error de negocio.
- Error genérico inesperado.
- JSON mal formado.
- Parámetros inválidos.

Formato recomendado de error:

```json
{
  "timestamp": "2026-08-10T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Surf spot not found with id 12",
  "path": "/api/surf-spots/12"
}
```

## Testing

Prioridades de testing:

- Unit tests para servicios CRUD.
- Unit tests para scoring de surf.
- Unit tests para scoring de ski.
- Tests de validación de reglas de negocio.
- Tests de integración para repositories con Testcontainers en fase avanzada.
- Tests de controller si aportan valor.

No buscar cobertura artificial. Priorizar tests que defiendan lógica real.

## Docker Compose

El proyecto debe incluir `docker-compose.yml` para levantar PostgreSQL localmente.

Variables recomendadas:

```text
POSTGRES_DB=surfsnow
POSTGRES_USER=surfsnow
POSTGRES_PASSWORD=surfsnow
```

La aplicación debe poder arrancar localmente contra esa base de datos.

## README Esperado

El README debe incluir:

- Descripción clara del proyecto.
- Motivación profesional.
- Stack técnico.
- Arquitectura.
- Funcionalidades actuales.
- Roadmap.
- Cómo ejecutar localmente.
- Cómo ejecutar tests.
- Endpoints principales.
- URL de Swagger local.
- Datos de ejemplo.
- Decisiones técnicas.
- Próximos pasos.
- URL de demo cuando exista.

## Roadmap Temporal

### Agosto 2026

Objetivo:

```text
Repositorio creado, proyecto Spring Boot inicial y PostgreSQL funcionando con Docker Compose.
```

Tareas:

- Crear repositorio.
- Crear README inicial.
- Crear proyecto Spring Boot.
- Configurar Java 21.
- Configurar PostgreSQL con Docker Compose.
- Crear estructura de paquetes.
- Añadir Flyway.
- Crear entidades iniciales.
- Preparar GitHub Project o issues.

### Septiembre 2026

Objetivo:

```text
CRUD funcional de surf spots y ski resorts.
```

Tareas:

- CRUD de surf spots.
- CRUD de ski resorts.
- DTOs.
- Validaciones.
- Mappers simples.
- Control global de errores.
- Swagger/OpenAPI.
- Tests unitarios iniciales.

### Octubre 2026

Objetivo:

```text
MVP funcional con forecast mockeado y scoring.
```

Tareas:

- Crear módulo forecast.
- Crear datos mock de forecast.
- Implementar surf score.
- Implementar ski score.
- Añadir endpoints de scoring.
- Añadir tests de servicios.
- Mejorar README.
- Preparar publicación de LinkedIn sobre MVP.

### Noviembre 2026

Objetivo:

```text
Proyecto backend serio con auth, tests de integración y CI.
```

Tareas:

- Añadir Spring Security.
- Implementar registro/login.
- Implementar JWT.
- Añadir favoritos.
- Añadir alertas básicas.
- Añadir Testcontainers.
- Configurar GitHub Actions.
- Preparar Dockerfile.

### Diciembre 2026

Objetivo:

```text
Proyecto desplegado y listo como portfolio item.
```

Tareas:

- Desplegar API.
- Configurar PostgreSQL gestionado.
- Configurar variables de entorno.
- Revisar Swagger público.
- Añadir URL de demo al README.
- Añadir diagrama de arquitectura.
- Documentar decisiones técnicas.

## Definición De Terminado Para El MVP

El MVP se considera terminado cuando:

- La aplicación arranca localmente.
- PostgreSQL se levanta con Docker Compose.
- Flyway crea el esquema.
- Existen endpoints CRUD para surf spots.
- Existen endpoints CRUD para ski resorts.
- Existe endpoint de forecast mockeado para surf.
- Existe endpoint de forecast mockeado para ski.
- Existe endpoint de score para surf.
- Existe endpoint de score para ski.
- Swagger funciona localmente.
- Hay tests unitarios relevantes.
- El README permite ejecutar el proyecto sin conocimiento previo.

## Criterio De Calidad Profesional

Este proyecto debe poder enseñarse en una entrevista técnica.

Por tanto, evitar:

- Código generado sin revisar.
- Entidades anémicas sin validaciones mínimas.
- Controladores con lógica de negocio.
- Servicios enormes y confusos.
- Excepciones sin manejar.
- README incompleto.
- Tests triviales que no prueban comportamiento.
- Configuración local difícil de reproducir.
- Endpoints inconsistentes.

Priorizar:

- Código simple.
- Nombres claros.
- Reglas de negocio localizadas.
- DTOs separados de entidades.
- Validación explícita.
- Errores predecibles.
- Tests útiles.
- Documentación honesta.
- Roadmap visible.

## Forma De Trabajar Con Opencode

Antes de implementar, inspeccionar el estado actual del repositorio.

Para cada cambio relevante:

- Entender la estructura existente.
- Hacer la modificación mínima correcta.
- Mantener coherencia con el estilo del proyecto.
- Ejecutar tests o al menos compilar.
- Actualizar README si cambia la forma de uso.
- No añadir funcionalidades avanzadas antes de cerrar el MVP.
- No introducir dependencias nuevas sin justificarlo.

Orden recomendado de implementación:

```text
1. Crear proyecto Spring Boot base.
2. Configurar PostgreSQL y Flyway.
3. Crear SurfSpot.
4. Crear SkiResort.
5. Añadir validaciones y errores globales.
6. Añadir Swagger.
7. Añadir forecast mockeado.
8. Añadir scoring.
9. Añadir tests.
10. Mejorar README.
11. Añadir auth y funcionalidades avanzadas.
12. Añadir CI/CD y despliegue.
```
