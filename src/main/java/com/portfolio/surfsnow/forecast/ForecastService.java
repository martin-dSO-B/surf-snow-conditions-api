package com.portfolio.surfsnow.forecast;

import com.portfolio.surfsnow.resort.SkiResort;
import com.portfolio.surfsnow.resort.SkiResortRepository;
import com.portfolio.surfsnow.shared.exception.ExternalForecastServiceException;
import com.portfolio.surfsnow.shared.exception.ResourceNotFoundException;
import com.portfolio.surfsnow.spot.SurfSpot;
import com.portfolio.surfsnow.spot.SurfSpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ForecastService {

    private final SurfSpotRepository surfSpotRepository;
    private final SkiResortRepository skiResortRepository;
    private final OpenMeteoClient openMeteoClient;
    private final OpenMeteoProperties properties;

    public ForecastService(
            SurfSpotRepository surfSpotRepository,
            SkiResortRepository skiResortRepository,
            OpenMeteoClient openMeteoClient,
            OpenMeteoProperties properties
    ) {
        this.surfSpotRepository = surfSpotRepository;
        this.skiResortRepository = skiResortRepository;
        this.openMeteoClient = openMeteoClient;
        this.properties = properties;
    }

    public SurfForecastResponse getSurfForecast(Long spotId) {
        return getSurfForecastData(spotId).toResponse();
    }

    public SkiForecastResponse getSkiForecast(Long resortId) {
        return getSkiForecastData(resortId).toResponse();
    }

    public SurfForecastData getSurfForecastData(Long spotId) {
        SurfSpot surfSpot = findSurfSpot(spotId);

        try {
            OpenMeteoClient.OpenMeteoWeather weather = openMeteoClient.fetchSurfWeather(
                    surfSpot.getLatitude(),
                    surfSpot.getLongitude()
            );
            OpenMeteoClient.OpenMeteoMarine marine = openMeteoClient.fetchSurfMarine(
                    surfSpot.getLatitude(),
                    surfSpot.getLongitude()
            );

            return new SurfForecastData(
                    surfSpot.getId(),
                    surfSpot.getName(),
                    weather.dateTime(),
                    weather.temperatureC(),
                    weather.windSpeedKmh(),
                    weather.windDirectionDegrees(),
                    ForecastDirection.toCompass(weather.windDirectionDegrees()),
                    weather.precipitationMm(),
                    marine.waveHeightM(),
                    marine.wavePeriodS(),
                    marine.swellWaveHeightM(),
                    marine.swellWavePeriodS(),
                    marine.swellWaveDirectionDegrees(),
                    ForecastDirection.toCompass(marine.swellWaveDirectionDegrees()),
                    DataSource.OPEN_METEO
            );
        } catch (ExternalForecastServiceException exception) {
            if (!properties.fallbackEnabled()) {
                throw exception;
            }
            return fallbackSurfForecast(surfSpot);
        }
    }

    public SkiForecastData getSkiForecastData(Long resortId) {
        SkiResort skiResort = findSkiResort(resortId);

        try {
            var forecasts = openMeteoClient.fetchSkiWeather(
                    skiResort.getLatitude(),
                    skiResort.getLongitude(),
                    skiResort.getBaseAltitude(),
                    skiResort.getMidAltitude(),
                    skiResort.getTopAltitude()
            );

            return new SkiForecastData(
                    skiResort.getId(),
                    skiResort.getName(),
                    skiResort.getBaseAltitude(),
                    skiResort.getMidAltitude(),
                    skiResort.getTopAltitude(),
                    toElevationForecast(ElevationType.BASE, skiResort.getBaseAltitude(), forecasts.get(0)),
                    toElevationForecast(ElevationType.MID, skiResort.getMidAltitude(), forecasts.get(1)),
                    toElevationForecast(ElevationType.TOP, skiResort.getTopAltitude(), forecasts.get(2)),
                    DataSource.OPEN_METEO
            );
        } catch (ExternalForecastServiceException exception) {
            if (!properties.fallbackEnabled()) {
                throw exception;
            }
            return fallbackSkiForecast(skiResort);
        }
    }

    private SurfSpot findSurfSpot(Long spotId) {
        return surfSpotRepository.findById(spotId)
                .orElseThrow(() -> new ResourceNotFoundException("Surf spot not found with id: " + spotId));
    }

    private SkiResort findSkiResort(Long resortId) {
        return skiResortRepository.findById(resortId)
                .orElseThrow(() -> new ResourceNotFoundException("Ski resort not found with id: " + resortId));
    }

    private SkiElevationForecastResponse toElevationForecast(
            ElevationType elevationType,
            Integer elevation,
            OpenMeteoClient.OpenMeteoWeather weather
    ) {
        return new SkiElevationForecastResponse(
                elevationType,
                elevation,
                weather.dateTime(),
                weather.temperatureC(),
                weather.apparentTemperatureC(),
                weather.windSpeedKmh(),
                weather.windDirectionDegrees(),
                ForecastDirection.toCompass(weather.windDirectionDegrees()),
                weather.precipitationMm(),
                weather.precipitationProbability(),
                weather.snowfallCm(),
                weather.snowDepthM(),
                weather.freezingLevelHeightM()
        );
    }

    private SurfForecastData fallbackSurfForecast(SurfSpot surfSpot) {
        long seed = surfSpot.getId();
        Integer windDirection = direction(seed, 5);
        Integer swellDirection = direction(seed, 7);

        return new SurfForecastData(
                surfSpot.getId(),
                surfSpot.getName(),
                "mock-current",
                round(5 + value(seed, 19, 20)),
                round(5 + value(seed, 13, 35)),
                windDirection,
                ForecastDirection.toCompass(windDirection),
                round(value(seed, 17, 12)),
                round(0.6 + value(seed, 23, 1.8)),
                round(6 + value(seed, 29, 9)),
                round(0.4 + value(seed, 31, 1.6)),
                round(6 + value(seed, 37, 10)),
                swellDirection,
                ForecastDirection.toCompass(swellDirection),
                DataSource.MOCK_FALLBACK
        );
    }

    private SkiForecastData fallbackSkiForecast(SkiResort skiResort) {
        long seed = skiResort.getId();
        SkiElevationForecastResponse base = fallbackSkiElevation(ElevationType.BASE, skiResort.getBaseAltitude(), seed, -6, 14);
        SkiElevationForecastResponse mid = fallbackSkiElevation(ElevationType.MID, skiResort.getMidAltitude(), seed + 11, -8, 13);
        SkiElevationForecastResponse top = fallbackSkiElevation(ElevationType.TOP, skiResort.getTopAltitude(), seed + 23, -10, 13);

        return new SkiForecastData(
                skiResort.getId(),
                skiResort.getName(),
                skiResort.getBaseAltitude(),
                skiResort.getMidAltitude(),
                skiResort.getTopAltitude(),
                base,
                mid,
                top,
                DataSource.MOCK_FALLBACK
        );
    }

    private SkiElevationForecastResponse fallbackSkiElevation(
            ElevationType type,
            Integer elevation,
            long seed,
            double minTemperature,
            double temperatureRange
    ) {
        Integer windDirection = direction(seed, 3);
        double temperature = round(minTemperature + value(seed, 19, temperatureRange));

        return new SkiElevationForecastResponse(
                type,
                elevation,
                "mock-current",
                temperature,
                round(temperature - value(seed, 7, 3)),
                round(5 + value(seed, 11, 40)),
                windDirection,
                ForecastDirection.toCompass(windDirection),
                round(value(seed, 13, 15)),
                (int) Math.round(value(seed, 17, 100)),
                round(value(seed, 23, 20)),
                round(value(seed, 29, 1.5)),
                round(elevation - 500 + value(seed, 31, 2000))
        );
    }

    private double value(long seed, int multiplier, double range) {
        return Math.floorMod(seed * multiplier, 100) / 100.0 * range;
    }

    private Integer direction(long seed, int multiplier) {
        return (int) (Math.floorMod(seed * multiplier, 8) * 45);
    }

    private Double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
