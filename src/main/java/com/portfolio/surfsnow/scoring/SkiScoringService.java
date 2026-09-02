package com.portfolio.surfsnow.scoring;

import com.portfolio.surfsnow.forecast.ForecastService;
import com.portfolio.surfsnow.forecast.SkiElevationForecastResponse;
import com.portfolio.surfsnow.forecast.SkiForecastData;
import org.springframework.stereotype.Service;

@Service
public class SkiScoringService {

    private static final double BASE_WEIGHT = 0.25;
    private static final double MID_WEIGHT = 0.45;
    private static final double TOP_WEIGHT = 0.30;

    private final ForecastService forecastService;

    public SkiScoringService(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    public SkiScoreResponse getSkiScore(Long resortId) {
        SkiForecastData forecast = forecastService.getSkiForecastData(resortId);
        double score = round(clamp(
                snowScore(forecast)
                        + temperatureScore(forecast)
                        + windScore(forecast)
                        + altitudeScore(forecast.topAltitude())
                        + weatherScore(forecast)
        ));

        return new SkiScoreResponse(
                forecast.resortId(),
                forecast.resortName(),
                forecast.baseAltitude(),
                forecast.midAltitude(),
                forecast.topAltitude(),
                forecast.base(),
                forecast.mid(),
                forecast.top(),
                score,
                recommendation(score),
                forecast.dataSource()
        );
    }

    private double snowScore(SkiForecastData forecast) {
        return weighted(
                elevationSnowScore(forecast.base()),
                elevationSnowScore(forecast.mid()),
                elevationSnowScore(forecast.top())
        );
    }

    private double elevationSnowScore(SkiElevationForecastResponse forecast) {
        double score;
        if (forecast.snowDepthM() >= 0.3) {
            score = 2.8;
        } else if (forecast.snowDepthM() >= 0.1) {
            score = 2.0;
        } else if (forecast.snowDepthM() > 0) {
            score = 1.0;
        } else {
            score = 0.2;
        }
        if (forecast.snowfallCm() > 0) {
            score += 0.7;
        }
        return Math.min(score, 3.5);
    }

    private double temperatureScore(SkiForecastData forecast) {
        return weighted(
                elevationTemperatureScore(forecast.base()),
                elevationTemperatureScore(forecast.mid()),
                elevationTemperatureScore(forecast.top())
        );
    }

    private double elevationTemperatureScore(SkiElevationForecastResponse forecast) {
        double temperature = forecast.temperatureC();
        if (temperature >= -8 && temperature <= 1) {
            return 2.0;
        }
        if (temperature > 1 && temperature <= 4) {
            return 1.2;
        }
        if (temperature < -15) {
            return 0.7;
        }
        if (temperature > 4) {
            return 0.3;
        }
        return 1.4;
    }

    private double windScore(SkiForecastData forecast) {
        return weighted(
                elevationWindScore(forecast.base()),
                elevationWindScore(forecast.mid()),
                elevationWindScore(forecast.top()) * 0.85
        );
    }

    private double elevationWindScore(SkiElevationForecastResponse forecast) {
        double windSpeed = forecast.windSpeedKmh();
        if (windSpeed <= 20) {
            return 1.5;
        }
        if (windSpeed <= 35) {
            return 1.0;
        }
        if (windSpeed <= 45) {
            return 0.5;
        }
        return 0.1;
    }

    private double altitudeScore(Integer topAltitude) {
        if (topAltitude >= 2500) {
            return 1.5;
        }
        if (topAltitude >= 2000) {
            return 1.0;
        }
        return 0.5;
    }

    private double weatherScore(SkiForecastData forecast) {
        return weighted(
                elevationWeatherScore(forecast.base()),
                elevationWeatherScore(forecast.mid()),
                elevationWeatherScore(forecast.top())
        );
    }

    private double elevationWeatherScore(SkiElevationForecastResponse forecast) {
        double score = 1.5;
        boolean rainLikely = forecast.precipitationMm() > 3 && forecast.temperatureC() > 1;
        boolean highFreezingLevel = forecast.freezingLevelHeightM() > forecast.elevation() + 400 && forecast.precipitationMm() > 1;
        boolean snowLikely = forecast.snowfallCm() > 0
                || (forecast.precipitationProbability() >= 50 && forecast.temperatureC() <= 1 && forecast.freezingLevelHeightM() <= forecast.elevation() + 300);

        if (rainLikely) {
            score -= 0.8;
        }
        if (highFreezingLevel) {
            score -= 0.5;
        }
        if (snowLikely) {
            score += 0.3;
        }
        return Math.max(0.0, Math.min(1.5, score));
    }

    private double weighted(double baseScore, double midScore, double topScore) {
        return baseScore * BASE_WEIGHT + midScore * MID_WEIGHT + topScore * TOP_WEIGHT;
    }

    private String recommendation(double score) {
        if (score >= 8.0) {
            return "Excellent ski conditions";
        }
        if (score >= 6.5) {
            return "Good ski conditions";
        }
        if (score >= 4.5) {
            return "Limited or variable ski conditions";
        }
        return "Poor ski conditions";
    }

    private double clamp(double score) {
        return Math.max(0.0, Math.min(10.0, score));
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
