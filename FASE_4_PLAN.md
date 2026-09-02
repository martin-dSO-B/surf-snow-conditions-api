# Fase 4 - Forecast Y Scoring Con Open-Meteo

## Objetivo

Implementar forecasts reales y scoring para surf y ski usando Open-Meteo como fuente principal de datos meteorologicos, marinos y de nieve.

Esta fase debe convertir la API en una aplicacion funcional con datos reales, manteniendo el alcance MVP, la arquitectura simple y el enfoque profesional de portfolio.

## Estado Inicial

- Los paquetes `com.portfolio.surfsnow.forecast` y `com.portfolio.surfsnow.scoring` existen, pero solo contienen `package-info.java`.
- No hay DTOs de forecast.
- No hay servicios de forecast.
- No hay servicios de scoring.
- No hay endpoints `/api/forecasts/...`.
- No hay endpoints `/api/scores/...`.
- El README ya anuncia endpoints de forecasts y scores, pero todavia no existen en codigo.

## Decision Tecnica

Usar Open-Meteo como proveedor real-first.

Open-Meteo encaja bien en el MVP porque:

- Tiene API gratuita para uso no comercial.
- No requiere API key en el free tier.
- Permite consultar por latitud y longitud, que ya existen en `SurfSpot` y `SkiResort`.
- Tiene forecast meteorologico general.
- Tiene forecast marino con altura de ola, periodo y swell.
- Tiene variables utiles para ski como temperatura, viento, precipitacion, snowfall y snow depth.
- Devuelve JSON simple.
- Puede integrarse con `RestClient` de Spring sin anadir dependencias nuevas.

## Fuentes De Referencia

- `Proyecto1.MD`: define MVP, endpoints y reglas iniciales de scoring.
- Open-Meteo Weather Forecast API: `https://open-meteo.com/en/docs`.
- Open-Meteo Marine Weather API: `https://open-meteo.com/en/docs/marine-weather-api`.
- Open-Meteo Pricing: free tier no comercial con limites publicados.
- Documentacion Spring Framework: `@RequestMapping`, `@GetMapping`, `@PathVariable` para controladores REST.
- Documentacion Spring Framework: `@Service` como estereotipo para capa de servicio.
- Documentacion Spring Framework: `RestClient` como cliente HTTP sincrono recomendado para aplicaciones imperativas.
- Documentacion Spring Boot: inyeccion de `RestClient.Builder` preconfigurado.
- NOAA/National Weather Service: escala Beaufort para rangos de viento y severidad.
- NOAA/National Weather Service: wind chill y efecto de viento/frio en percepcion termica.

## Limites Y Licencia Open-Meteo

Uso previsto para este proyecto:

- Portfolio personal.
- Demo tecnica.
- Uso no comercial.

Limites del free tier segun Open-Meteo:

```text
600 calls/min
5.000 calls/hour
10.000 calls/day
300.000 calls/month
```

Condiciones relevantes:

- El free tier no tiene garantia de uptime.
- Para uso comercial se requiere plan comercial.
- Open-Meteo indica que los datos requieren atribucion porque se basan en fuentes open data con licencia CC BY 4.0.
- La documentacion del README debera incluir atribucion a Open-Meteo y a sus proveedores de datos cuando aplique.

## Principios De Implementacion

- Usar Open-Meteo como fuente principal de forecasts.
- Mantener fallback mockeado solo como resiliencia si Open-Meteo falla o devuelve datos incompletos.
- No crear tablas nuevas para forecasts o scores en esta fase.
- Crear una migracion Flyway previa para anadir cota intermedia a `ski_resorts`.
- No persistir forecasts ni scores en esta fase.
- No anadir dependencias nuevas salvo que aparezca una necesidad tecnica real.
- Mantener controllers finos y logica en servicios.
- Usar DTOs con records para respuestas simples.
- Reutilizar repositorios existentes para validar que `spotId` y `resortId` existen.
- Reutilizar `ResourceNotFoundException` para respuestas 404.
- Encapsular llamadas externas en un cliente propio dentro del modulo `forecast`.
- Evitar exponer directamente el JSON externo de Open-Meteo en la API publica.
- Mantener los endpoints internos estables aunque cambie el proveedor externo.

## Paso Previo - Modelo De Ski Resorts Con Tres Cotas

### Motivo

Open-Meteo no adapta automaticamente una estacion de esqui completa. La API calcula el forecast para las coordenadas y elevacion enviadas. Por defecto usa un modelo digital de elevacion de 90 m, pero tambien permite pasar manualmente `elevation={metros}`.

Para estaciones de esqui, una unica elevacion no representa bien la realidad porque temperatura, nieve, viento, cota de nieve y condiciones pueden variar mucho entre la base y la cota alta.

Decision: representar explicitamente tres cotas en `SkiResort`.

```text
baseAltitude   -> cota inferior
midAltitude    -> cota intermedia
topAltitude    -> cota alta
```

### Estado Actual Del Modelo

Actualmente `SkiResort` tiene:

```text
baseAltitude
topAltitude
```

No existe:

```text
midAltitude
```

Por tanto hay que modificar:

- Entidad `SkiResort`.
- DTO `SkiResortRequest`.
- DTO `SkiResortResponse`.
- Mapeo y validacion en `SkiResortService`.
- Esquema de base de datos con una nueva migracion Flyway.
- Seed data existente mediante migracion incremental, no modificando migraciones ya aplicadas.
- README si documenta ejemplos de ski resorts.

### Cambio En Base De Datos

No se deben editar `V1__create_initial_schema.sql` ni `V2__insert_seed_data.sql` si ya han sido aplicadas por Flyway en la base del contenedor Docker.

Crear nueva migracion:

```text
V3__add_mid_altitude_to_ski_resorts.sql
```

Contenido propuesto:

```sql
ALTER TABLE ski_resorts
    ADD COLUMN mid_altitude INTEGER;

UPDATE ski_resorts
SET mid_altitude = ((base_altitude + top_altitude) / 2);

ALTER TABLE ski_resorts
    ALTER COLUMN mid_altitude SET NOT NULL;

ALTER TABLE ski_resorts
    ADD CONSTRAINT ck_ski_resorts_mid_altitude CHECK (mid_altitude > 0),
    ADD CONSTRAINT ck_ski_resorts_mid_altitude_range CHECK (
        mid_altitude >= base_altitude AND mid_altitude <= top_altitude
    );
```

Impacto en Docker/PostgreSQL:

- Si el contenedor y el volumen actual siguen levantados, Flyway detectara `V3__add_mid_altitude_to_ski_resorts.sql` como nueva migracion pendiente.
- No hace falta borrar el contenedor ni el volumen.
- No se deben modificar migraciones anteriores porque Flyway validaria checksums y podria fallar el arranque.
- Al arrancar la aplicacion, Flyway aplicara `V3` sobre la base existente.
- Si la aplicacion ya esta arrancada, habra que reiniciarla para que Flyway ejecute la nueva migracion.

### Cambio En Entidad

Actualizar `SkiResort`:

```text
private Integer midAltitude;
```

Mapeo JPA:

```text
@Column(name = "mid_altitude", nullable = false)
```

Actualizar constructor, getter y setter.

### Cambio En DTOs CRUD

Actualizar `SkiResortRequest`:

```text
@NotNull
@Positive
Integer midAltitude
```

Actualizar `SkiResortResponse`:

```text
Integer midAltitude
```

### Cambio En Validacion De Servicio

Actualizar `SkiResortService.validateAltitudeRange`.

Regla nueva:

```text
baseAltitude <= midAltitude <= topAltitude
```

Mensaje propuesto:

```text
Middle altitude must be greater than or equal to base altitude and less than or equal to top altitude
```

### Datos Existentes

Para datos ya existentes, `mid_altitude` se calculara automaticamente con la migracion como punto medio entre base y cima.

Valores derivados actuales:

```text
Baqueira Beret: base 1500, mid 2055, top 2610
Sierra Nevada: base 2100, mid 2700, top 3300
Formigal: base 1500, mid 1875, top 2250
Grandvalira: base 1710, mid 2175, top 2640
```

Mas adelante se podrian sustituir por cotas intermedias reales si se quiere mas precision. Para el MVP es suficiente y consistente.

### Impacto En Open-Meteo

El forecast de ski debe consultar Open-Meteo para tres elevaciones:

```text
elevation=baseAltitude
elevation=midAltitude
elevation=topAltitude
```

Open-Meteo permite multiples coordenadas y elevaciones separadas por coma. Hay dos opciones validas:

```text
Opcion A: una unica llamada con tres puntos
latitude={lat},{lat},{lat}
longitude={lon},{lon},{lon}
elevation={baseAltitude},{midAltitude},{topAltitude}
```

```text
Opcion B: tres llamadas separadas
elevation={baseAltitude}
elevation={midAltitude}
elevation={topAltitude}
```

Decision recomendada: usar una unica llamada con tres puntos para ski.

Motivos:

- Reduce llamadas externas.
- Respeta mejor limites del free tier.
- Devuelve una lista de respuestas equivalente a cada coordenada/elevacion.
- Mantiene el endpoint interno eficiente.

Si el mapeo de respuesta multiple complica demasiado el MVP, se aceptaria empezar con tres llamadas separadas por claridad, pero la opcion preferida es una llamada multiple.

## Endpoints Externos Open-Meteo

### Weather Forecast API

Base URL:

```text
https://api.open-meteo.com/v1/forecast
```

Uso para surf:

```text
temperature_2m
precipitation
wind_speed_10m
wind_direction_10m
```

Uso para ski:

```text
temperature_2m
apparent_temperature
precipitation
precipitation_probability
snowfall
snow_depth
freezing_level_height
wind_speed_10m
wind_direction_10m
```

Parametros comunes:

```text
latitude={latitude}
longitude={longitude}
current={variables}
wind_speed_unit=kmh
timezone=auto
```

Parametros especificos para ski con tres cotas:

```text
latitude={latitude},{latitude},{latitude}
longitude={longitude},{longitude},{longitude}
elevation={baseAltitude},{midAltitude},{topAltitude}
current=temperature_2m,apparent_temperature,precipitation,precipitation_probability,snowfall,snow_depth,freezing_level_height,wind_speed_10m,wind_direction_10m
wind_speed_unit=kmh
timezone=auto
```

Ejemplo surf weather:

```text
https://api.open-meteo.com/v1/forecast?latitude=43.4086&longitude=-2.6986&current=temperature_2m,precipitation,wind_speed_10m,wind_direction_10m&wind_speed_unit=kmh&timezone=auto
```

Ejemplo ski weather:

```text
https://api.open-meteo.com/v1/forecast?latitude=42.6986,42.6986,42.6986&longitude=0.9347,0.9347,0.9347&elevation=1500,2055,2610&current=temperature_2m,apparent_temperature,precipitation,precipitation_probability,snowfall,snow_depth,freezing_level_height,wind_speed_10m,wind_direction_10m&wind_speed_unit=kmh&timezone=auto
```

### Marine Weather API

Base URL:

```text
https://marine-api.open-meteo.com/v1/marine
```

Uso para surf:

```text
wave_height
wave_period
swell_wave_height
swell_wave_period
swell_wave_direction
```

Parametros comunes:

```text
latitude={latitude}
longitude={longitude}
current={variables}
timezone=auto
```

Ejemplo surf marine:

```text
https://marine-api.open-meteo.com/v1/marine?latitude=43.4086&longitude=-2.6986&current=wave_height,wave_period,swell_wave_height,swell_wave_period,swell_wave_direction&timezone=auto
```

## Respuestas Probadas

Open-Meteo Weather API respondio correctamente para Mundaka con datos como:

```text
temperature_2m: 19.0
precipitation: 0.00
wind_speed_10m: 1.4
wind_direction_10m: 180
```

Open-Meteo Marine API respondio correctamente para Mundaka con datos como:

```text
wave_height: 0.60
wave_period: 6.30
swell_wave_height: 0.52
swell_wave_period: 5.80
swell_wave_direction: 315
```

Open-Meteo Weather API respondio correctamente para Baqueira Beret con datos como:

```text
temperature_2m: 15.3
precipitation: 0.00
snowfall: 0.00
snow_depth: 0.00
wind_speed_10m: 1.5
wind_direction_10m: 104
```

## Diseno De Integracion HTTP

Crear un cliente interno:

```text
OpenMeteoClient
```

Ubicacion propuesta:

```text
com.portfolio.surfsnow.forecast
```

Responsabilidades:

- Construir URLs hacia Open-Meteo Weather API.
- Construir URLs hacia Open-Meteo Marine API.
- Ejecutar peticiones GET con `RestClient`.
- Mapear la respuesta externa a records internos.
- Detectar respuestas nulas o incompletas.
- Lanzar una excepcion interna o devolver un resultado controlado cuando el proveedor falle.

Usar `RestClient.Builder` inyectado por Spring Boot:

```text
RestClient.Builder
```

Motivo:

- Spring Framework recomienda `RestClient` para clientes HTTP sincronos en aplicaciones imperativas.
- Este proyecto usa Spring MVC clasico, no WebFlux.
- `spring-boot-starter-web` ya aporta lo necesario.

## Configuracion Propuesta

Anadir propiedades en `application.yml`:

```yaml
open-meteo:
  weather-base-url: https://api.open-meteo.com/v1/forecast
  marine-base-url: https://marine-api.open-meteo.com/v1/marine
  fallback-enabled: true
```

Crear `OpenMeteoProperties` solo si aporta claridad suficiente.

Decision recomendada:

- Si hay pocas propiedades, usar `@Value` para mantenerlo simple.
- Si se anaden timeouts, fallback y mas opciones, usar `@ConfigurationProperties`.

Para esta fase se recomienda `@ConfigurationProperties` porque deja la integracion mas profesional y facil de extender sin ensuciar el cliente.

## Modelo Interno De Forecast

Separar respuesta publica de datos internos para scoring.

Records internos propuestos:

```text
SurfForecastData
SkiForecastData
```

Estos records podran contener campos adicionales utiles para scoring, aunque no todos se expongan igual en la respuesta publica.

## Modulo Forecast

### DTOs Publicos

Crear `SurfForecastResponse` con estos campos:

```text
spotId
spotName
dateTime
temperatureC
windSpeedKmh
windDirectionDegrees
windDirectionCompass
precipitationMm
waveHeightM
wavePeriodS
swellWaveHeightM
swellWavePeriodS
swellWaveDirectionDegrees
swellWaveDirectionCompass
dataSource
```

Crear `SkiForecastResponse` con estos campos:

```text
resortId
resortName
base
mid
top
dataSource
```

Cada bloque `base`, `mid` y `top` usara un DTO anidado o record reutilizable:

```text
SkiElevationForecastResponse
```

Campos:

```text
elevationType
elevation
dateTime
temperatureC
apparentTemperatureC
windSpeedKmh
windDirectionDegrees
windDirectionCompass
precipitationMm
precipitationProbability
snowfallCm
snowDepthM
freezingLevelHeightM
```

Notas:

- `dateTime` representa el timestamp devuelto por Open-Meteo para `current.time`.
- `dataSource` valdra `OPEN_METEO` cuando los datos vengan del proveedor real.
- `dataSource` valdra `MOCK_FALLBACK` si se usa fallback.
- Se incluyen direcciones en grados y en texto cardinal para mejorar legibilidad.
- En ski, `base`, `mid` y `top` representan cota inferior, intermedia y alta.

### Servicio

Crear `ForecastService`.

Responsabilidades:

- Validar que existe el `SurfSpot` usando `SurfSpotRepository`.
- Validar que existe el `SkiResort` usando `SkiResortRepository`.
- Lanzar `ResourceNotFoundException` cuando no exista el recurso local.
- Consultar Open-Meteo para forecasts reales.
- Combinar weather + marine para surf.
- Usar weather para ski consultando base, cota intermedia y cota alta.
- Aplicar fallback mockeado si Open-Meteo falla y `fallback-enabled=true`.
- Devolver DTOs publicos para los endpoints.
- Proveer datos internos al modulo scoring.

### Surf Forecast

Flujo:

```text
GET /api/forecasts/surf/{spotId}
-> ForecastController
-> ForecastService
-> SurfSpotRepository.findById(spotId)
-> OpenMeteoClient.getCurrentWeather(latitude, longitude)
-> OpenMeteoClient.getCurrentMarine(latitude, longitude)
-> combinar datos
-> SurfForecastResponse
```

Datos Open-Meteo usados:

```text
Weather API current.temperature_2m
Weather API current.precipitation
Weather API current.wind_speed_10m
Weather API current.wind_direction_10m
Marine API current.wave_height
Marine API current.wave_period
Marine API current.swell_wave_height
Marine API current.swell_wave_period
Marine API current.swell_wave_direction
```

### Ski Forecast

Flujo:

```text
GET /api/forecasts/ski/{resortId}
-> ForecastController
-> ForecastService
-> SkiResortRepository.findById(resortId)
-> OpenMeteoClient.getCurrentWeatherForSki(latitude, longitude, baseAltitude, midAltitude, topAltitude)
-> SkiForecastResponse
```

Datos Open-Meteo usados:

```text
Weather API current.temperature_2m
Weather API current.apparent_temperature
Weather API current.precipitation
Weather API current.precipitation_probability
Weather API current.snowfall
Weather API current.snow_depth
Weather API current.freezing_level_height
Weather API current.wind_speed_10m
Weather API current.wind_direction_10m
```

Cada variable se obtendra para tres elevaciones:

```text
baseAltitude
midAltitude
topAltitude
```

### Controller

Crear `ForecastController`.

Endpoints:

```text
GET /api/forecasts/surf/{spotId}
GET /api/forecasts/ski/{resortId}
```

OpenAPI:

```text
@Tag(name = "Forecasts")
@Operation(summary = "Get current surf forecast")
@Operation(summary = "Get current ski forecast")
```

## Modulo Scoring

### DTOs Publicos

Crear `SurfScoreResponse` con estos campos:

```text
spotId
spotName
dateTime
waveHeightM
wavePeriodS
swellWaveHeightM
swellWavePeriodS
windSpeedKmh
windDirectionDegrees
windDirectionCompass
temperatureC
precipitationMm
surfScore
recommendation
dataSource
```

Crear `SkiScoreResponse` con estos campos:

```text
resortId
resortName
baseAltitude
midAltitude
topAltitude
baseForecast
midForecast
topForecast
skiScore
recommendation
dataSource
```

Los bloques `baseForecast`, `midForecast` y `topForecast` podran reutilizar el mismo record de forecast por cota o un record especifico de scoring si se quiere una respuesta mas compacta.

## Surf Scoring

Crear `SurfScoringService`.

Responsabilidades:

- Obtener forecast real desde `ForecastService`.
- Calcular score entre `0.0` y `10.0`.
- Redondear a 1 decimal.
- Generar recomendacion textual.
- Mantener la formula explicita y testeable.

Formula base:

```text
score = waveScore + periodScore + windScore + weatherScore
```

Pesos propuestos:

```text
waveScore: 0 - 3.5
periodScore: 0 - 2.5
windScore: 0 - 2.0
weatherScore: 0 - 2.0
```

Criterios:

- Usar `wave_height` como altura principal de ola.
- Usar `wave_period` como periodo principal.
- Usar `swell_wave_height` y `swell_wave_period` como soporte para mejorar o matizar la puntuacion.
- Olas entre `0.8m` y `2.0m` puntuan alto para un MVP generalista.
- Periodo `>= 10s` mejora mucho el score.
- Viento fuerte penaliza segun rangos inspirados en Beaufort.
- Mucha precipitacion penaliza.
- Temperatura fria penaliza ligeramente.

Reglas iniciales sugeridas:

```text
waveHeightM < 0.5      -> waveScore bajo
0.8 <= waveHeightM <= 2.0 -> waveScore alto
waveHeightM > 3.0      -> penalizacion por condiciones exigentes
wavePeriodS < 7        -> periodScore bajo
wavePeriodS >= 10      -> periodScore alto
windSpeedKmh <= 15     -> windScore alto
windSpeedKmh >= 35     -> windScore bajo
precipitationMm >= 5   -> penalizacion weather
temperatureC < 8       -> penalizacion ligera
```

Recomendaciones textuales:

```text
score >= 8.0 -> Excellent surf conditions
score >= 6.5 -> Good surf conditions
score >= 4.5 -> Usable but mixed surf conditions
score < 4.5  -> Poor surf conditions
```

## Ski Scoring

Crear `SkiScoringService`.

Responsabilidades:

- Obtener forecast real desde `ForecastService`.
- Usar datos de estacion y forecasts por cota: inferior, intermedia y alta.
- Calcular score entre `0.0` y `10.0`.
- Redondear a 1 decimal.
- Generar recomendacion textual.
- Mantener la formula explicita y testeable.

Formula base:

```text
score = snowScore + temperatureScore + windScore + altitudeScore + weatherScore
```

Pesos propuestos:

```text
snowScore: 0 - 3.5
temperatureScore: 0 - 2.0
windScore: 0 - 1.5
altitudeScore: 0 - 1.5
weatherScore: 0 - 1.5
```

Criterios:

- Usar `snow_depth` por cota como indicador principal de nieve disponible.
- Usar `snowfall` por cota como indicador de nieve reciente.
- Usar `freezing_level_height` para estimar si la cota de cero grados esta por encima o por debajo de cada zona de la estacion.
- Usar `precipitation_probability` como probabilidad de precipitacion, no como probabilidad directa de nieve.
- Derivar una senal de nieve probable combinando `temperature_2m`, `freezing_level_height`, `snowfall` y `precipitation_probability`.
- Temperatura entre `-8C` y `1C` favorece, especialmente en cota media y alta.
- Viento fuerte penaliza, especialmente en cota alta.
- Estaciones con mayor altitud reciben bonus moderado.
- Lluvia con temperatura positiva penaliza, especialmente en base y cota intermedia.
- Condiciones extremas deben penalizar aunque haya nieve.

Reglas iniciales sugeridas:

```text
snowDepthM >= 0.3      -> snowScore alto
snowDepthM == 0        -> snowScore bajo
snowfallCm > 0         -> bonus de nieve reciente
-8 <= temperatureC <= 1 -> temperatureScore alto
temperatureC > 4       -> penalizacion por nieve humeda o lluvia
windSpeedKmh <= 20     -> windScore alto
windSpeedKmh >= 45     -> windScore bajo
topAltitude >= 2500    -> altitudeScore alto
precipitationMm > 3 && temperatureC > 1 -> penalizacion por lluvia
```

Reglas con tres cotas:

```text
baseForecast aporta menos peso que midForecast y topForecast.
midForecast representa condiciones generales de la estacion.
topForecast representa conservacion de nieve y exposicion al viento.
snowScore = ponderacion de snowDepthM y snowfallCm en base/mid/top.
temperatureScore = ponderacion de temperatura en base/mid/top.
windScore = penalizacion ponderada, con mayor peso en topForecast.
weatherScore = penalizacion por lluvia probable y cota de cero grados alta.
```

Pesos internos recomendados por cota:

```text
base: 25%
mid: 45%
top: 30%
```

Motivo:

- La cota baja importa para accesos, retorno y calidad de nieve en la base.
- La cota media suele representar mejor el dominio esquiable general.
- La cota alta importa para nieve, pero puede estar mas afectada por viento o condiciones extremas.

Recomendaciones textuales:

```text
score >= 8.0 -> Excellent ski conditions
score >= 6.5 -> Good ski conditions
score >= 4.5 -> Limited or variable ski conditions
score < 4.5  -> Poor ski conditions
```

### Controller

Crear `ScoreController`.

Endpoints:

```text
GET /api/scores/surf/{spotId}
GET /api/scores/ski/{resortId}
```

OpenAPI:

```text
@Tag(name = "Scores")
@Operation(summary = "Get current surf condition score")
@Operation(summary = "Get current ski condition score")
```

## Fallback Mockeado

Mantener fallback mockeado para que la demo siga funcionando si:

- Open-Meteo no responde.
- Hay timeout.
- La respuesta no contiene algun campo imprescindible.
- La Marine API no devuelve datos para una coordenada costera concreta.

El fallback debe:

- Ser determinista por ID.
- Usar rangos realistas.
- Indicar `dataSource=MOCK_FALLBACK` en la respuesta.
- No ocultar completamente que se ha usado fallback.

Rangos fallback para surf:

```text
waveHeightM: 0.6 - 2.4
wavePeriodS: 6 - 15
swellWaveHeightM: 0.4 - 2.0
swellWavePeriodS: 6 - 16
windSpeedKmh: 5 - 40
temperatureC: 5 - 24
precipitationMm: 0 - 12
```

Rangos fallback para ski:

```text
base.temperatureC: -6 - 8
mid.temperatureC: -8 - 5
top.temperatureC: -10 - 3
windSpeedKmh: 5 - 45
precipitationMm: 0 - 15
precipitationProbability: 0 - 100
snowfallCm: 0 - 20
snowDepthM: 0 - 1.5
freezingLevelHeightM: baseAltitude - 500 hasta topAltitude + 1500
```

## Manejo De Errores

Errores locales:

- Si `spotId` no existe, devolver 404 mediante `ResourceNotFoundException`.
- Si `resortId` no existe, devolver 404 mediante `ResourceNotFoundException`.

Errores externos:

- Si Open-Meteo falla y `fallback-enabled=true`, devolver fallback con `dataSource=MOCK_FALLBACK`.
- Si Open-Meteo falla y `fallback-enabled=false`, devolver error controlado 503.

Para el caso 503 se propone crear una excepcion propia:

```text
ExternalForecastServiceException
```

Y manejarla en `GlobalExceptionHandler` como:

```text
HTTP 503 Service Unavailable
title: Forecast provider unavailable
detail: Forecast data could not be retrieved from the external provider
```

## Conversion De Direcciones

Open-Meteo devuelve direcciones en grados.

Crear una funcion simple para convertir grados a direccion cardinal:

```text
0/360 -> N
45    -> NE
90    -> E
135   -> SE
180   -> S
225   -> SW
270   -> W
315   -> NW
```

La conversion puede vivir inicialmente como metodo privado del servicio o cliente. Solo extraerla a helper si se reutiliza en varios sitios.

## Redondeo

Usar redondeo consistente para respuestas:

```text
score: 1 decimal
metricas meteorologicas: usar los valores de Open-Meteo tal como llegan, salvo que sea necesario normalizar
```

## Documentacion

Actualizar `README.md`:

- Estado actual de Fase 4.
- Endpoints MVP implementados.
- Ejemplos JSON breves de forecast y score.
- Aclarar que los forecasts usan Open-Meteo como proveedor real.
- Aclarar que existe fallback mockeado para resiliencia.
- Incluir atribucion a Open-Meteo.
- Documentar que no se necesita API key.
- Documentar limites relevantes del free tier.

Ejemplo de atribucion recomendada:

```text
Weather and marine forecast data provided by Open-Meteo (https://open-meteo.com/).
Open-Meteo data is based on open weather data sources and requires attribution under CC BY 4.0 where applicable.
```

## Checklist PROJECT_TASKS.md

Actualizar Fase 4 para reflejar que sera real-first con Open-Meteo.

Checklist propuesto:

```text
- [ ] Paso previo: anadir `midAltitude` a ski resorts.
- [ ] Crear migracion Flyway `V3__add_mid_altitude_to_ski_resorts.sql`.
- [ ] Actualizar entidad `SkiResort` con `midAltitude`.
- [ ] Actualizar DTOs CRUD de `SkiResort` con `midAltitude`.
- [ ] Actualizar validacion `baseAltitude <= midAltitude <= topAltitude`.
- [ ] Crear modulo `forecast`.
- [ ] Integrar Open-Meteo Weather API.
- [ ] Integrar Open-Meteo Marine API.
- [ ] Crear forecast real para surf.
- [ ] Crear forecast real para ski con base, cota intermedia y cota alta.
- [ ] Crear fallback mockeado para forecasts.
- [ ] Anadir endpoint `/api/forecasts/surf/{spotId}`.
- [ ] Anadir endpoint `/api/forecasts/ski/{resortId}`.
- [ ] Crear modulo `scoring`.
- [ ] Implementar surf score.
- [ ] Implementar ski score.
- [ ] Anadir endpoint `/api/scores/surf/{spotId}`.
- [ ] Anadir endpoint `/api/scores/ski/{resortId}`.
- [ ] Documentar Open-Meteo y atribucion en README.
```

## Verificacion

Ejecutar al menos:

```powershell
mvn test
```

Verificacion manual recomendada con la aplicacion arrancada:

```text
GET /api/forecasts/surf/1
GET /api/forecasts/ski/1
GET /api/scores/surf/1
GET /api/scores/ski/1
```

Verificacion de migracion con Docker/PostgreSQL:

```text
1. Mantener el contenedor PostgreSQL existente.
2. Arrancar o reiniciar la aplicacion.
3. Confirmar que Flyway aplica `V3__add_mid_altitude_to_ski_resorts.sql`.
4. Confirmar que `ski_resorts.mid_altitude` existe y tiene valores no nulos.
5. Confirmar que Hibernate `ddl-auto=validate` no falla al arrancar.
```

Verificar tambien:

```text
GET /api/forecasts/surf/999 -> 404
GET /api/forecasts/ski/999 -> 404
```

Si las pruebas no pudieran ejecutarse, documentar el motivo y que se verifico en su lugar.

## Riesgos Y Mitigaciones

Riesgo: Open-Meteo no disponible.

Mitigacion: fallback mockeado con `dataSource=MOCK_FALLBACK`.

Riesgo: Marine API devuelve datos alejados de la costa o incompletos.

Mitigacion: fallback para variables marinas y documentar que el forecast marino es orientativo.

Riesgo: limites de free tier.

Mitigacion: el portfolio tiene bajo trafico; no usar llamadas masivas ni polling interno.

Riesgo: uso comercial futuro.

Mitigacion: documentar que el free tier es no comercial y que se requeriria plan comercial si el proyecto evolucionara.

Riesgo: tests dependientes de red.

Mitigacion: no hacer tests unitarios contra Open-Meteo real. Mockear el cliente HTTP en tests de servicios o probar solo logica de scoring con datos fijos.

## Fuera De Alcance En Esta Fase

- Persistir historico de forecasts.
- Cache con Redis.
- Scheduler para precargar forecasts.
- API keys comerciales.
- Integracion con varios proveedores simultaneos.
- Alertas.
- Favoritos.
- Autenticacion.
- Testcontainers.
- CI/CD.

## Decision Pendiente

Revisar este plan antes de implementar.

Si se aprueba, la implementacion de Fase 4 seguira este enfoque:

```text
Open-Meteo real-first + fallback mockeado + scoring calculado al vuelo + sin persistencia
```
