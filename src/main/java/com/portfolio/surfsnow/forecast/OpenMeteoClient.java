package com.portfolio.surfsnow.forecast;

import com.fasterxml.jackson.databind.JsonNode;
import com.portfolio.surfsnow.shared.exception.ExternalForecastServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
class OpenMeteoClient {

    private static final String SURF_WEATHER_VARIABLES = "temperature_2m,precipitation,wind_speed_10m,wind_direction_10m";
    private static final String SURF_MARINE_VARIABLES = "wave_height,wave_period,swell_wave_height,swell_wave_period,swell_wave_direction";
    private static final String SKI_WEATHER_VARIABLES = "temperature_2m,apparent_temperature,precipitation,precipitation_probability,snowfall,snow_depth,freezing_level_height,wind_speed_10m,wind_direction_10m";

    private final RestClient restClient;
    private final OpenMeteoProperties properties;

    OpenMeteoClient(RestClient.Builder restClientBuilder, OpenMeteoProperties properties) {
        this.restClient = restClientBuilder.build();
        this.properties = properties;
    }

    OpenMeteoWeather fetchSurfWeather(Double latitude, Double longitude) {
        JsonNode root = get(properties.weatherBaseUrl(), latitude, longitude, null, SURF_WEATHER_VARIABLES);
        JsonNode current = requiredCurrent(root);

        return new OpenMeteoWeather(
                text(current, "time"),
                decimal(current, "temperature_2m"),
                null,
                decimal(current, "precipitation"),
                null,
                null,
                null,
                decimal(current, "wind_speed_10m"),
                integer(current, "wind_direction_10m")
        );
    }

    OpenMeteoMarine fetchSurfMarine(Double latitude, Double longitude) {
        JsonNode root = get(properties.marineBaseUrl(), latitude, longitude, null, SURF_MARINE_VARIABLES);
        JsonNode current = requiredCurrent(root);

        return new OpenMeteoMarine(
                decimal(current, "wave_height"),
                decimal(current, "wave_period"),
                decimal(current, "swell_wave_height"),
                decimal(current, "swell_wave_period"),
                integer(current, "swell_wave_direction")
        );
    }

    List<OpenMeteoWeather> fetchSkiWeather(Double latitude, Double longitude, Integer baseAltitude, Integer midAltitude, Integer topAltitude) {
        JsonNode root = get(
                properties.weatherBaseUrl(),
                latitude + "," + latitude + "," + latitude,
                longitude + "," + longitude + "," + longitude,
                baseAltitude + "," + midAltitude + "," + topAltitude,
                SKI_WEATHER_VARIABLES
        );

        if (root == null || !root.isArray() || root.size() != 3) {
            throw new ExternalForecastServiceException("Forecast data could not be retrieved from the external provider");
        }

        return List.of(toSkiWeather(root.get(0)), toSkiWeather(root.get(1)), toSkiWeather(root.get(2)));
    }

    private JsonNode get(String baseUrl, Object latitude, Object longitude, Object elevation, String variables) {
        try {
            return restClient.get()
                    .uri(buildUri(baseUrl, latitude, longitude, elevation, variables))
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientException | IllegalArgumentException exception) {
            throw new ExternalForecastServiceException("Forecast data could not be retrieved from the external provider", exception);
        }
    }

    private URI buildUri(String baseUrl, Object latitude, Object longitude, Object elevation, String variables) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current", variables)
                .queryParam("wind_speed_unit", "kmh")
                .queryParam("timezone", "auto");
        if (elevation != null) {
            builder.queryParam("elevation", elevation);
        }
        return builder.build(false).toUri();
    }

    private OpenMeteoWeather toSkiWeather(JsonNode root) {
        JsonNode current = requiredCurrent(root);
        return new OpenMeteoWeather(
                text(current, "time"),
                decimal(current, "temperature_2m"),
                decimal(current, "apparent_temperature"),
                decimal(current, "precipitation"),
                integer(current, "precipitation_probability"),
                decimal(current, "snowfall"),
                decimal(current, "snow_depth"),
                decimal(current, "wind_speed_10m"),
                integer(current, "wind_direction_10m"),
                decimal(current, "freezing_level_height")
        );
    }

    private JsonNode requiredCurrent(JsonNode root) {
        if (root == null || !root.hasNonNull("current")) {
            throw new ExternalForecastServiceException("Forecast data could not be retrieved from the external provider");
        }
        return root.get("current");
    }

    private String text(JsonNode node, String field) {
        if (!node.hasNonNull(field)) {
            throw new ExternalForecastServiceException("Forecast data could not be retrieved from the external provider");
        }
        return node.get(field).asText();
    }

    private Double decimal(JsonNode node, String field) {
        if (!node.hasNonNull(field)) {
            throw new ExternalForecastServiceException("Forecast data could not be retrieved from the external provider");
        }
        return node.get(field).asDouble();
    }

    private Integer integer(JsonNode node, String field) {
        if (!node.hasNonNull(field)) {
            throw new ExternalForecastServiceException("Forecast data could not be retrieved from the external provider");
        }
        return node.get(field).asInt();
    }

    record OpenMeteoWeather(
            String dateTime,
            Double temperatureC,
            Double apparentTemperatureC,
            Double precipitationMm,
            Integer precipitationProbability,
            Double snowfallCm,
            Double snowDepthM,
            Double windSpeedKmh,
            Integer windDirectionDegrees,
            Double freezingLevelHeightM
    ) {
        OpenMeteoWeather(
                String dateTime,
                Double temperatureC,
                Double apparentTemperatureC,
                Double precipitationMm,
                Integer precipitationProbability,
                Double snowfallCm,
                Double snowDepthM,
                Double windSpeedKmh,
                Integer windDirectionDegrees
        ) {
            this(dateTime, temperatureC, apparentTemperatureC, precipitationMm, precipitationProbability, snowfallCm, snowDepthM, windSpeedKmh, windDirectionDegrees, null);
        }
    }

    record OpenMeteoMarine(
            Double waveHeightM,
            Double wavePeriodS,
            Double swellWaveHeightM,
            Double swellWavePeriodS,
            Integer swellWaveDirectionDegrees
    ) {
    }
}
