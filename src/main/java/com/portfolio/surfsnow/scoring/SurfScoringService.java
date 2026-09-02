package com.portfolio.surfsnow.scoring;

import com.portfolio.surfsnow.forecast.ForecastService;
import com.portfolio.surfsnow.forecast.SurfForecastData;
import org.springframework.stereotype.Service;

@Service
public class SurfScoringService {

    private final ForecastService forecastService;

    public SurfScoringService(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    public SurfScoreResponse getSurfScore(Long spotId) {
        SurfForecastData forecast = forecastService.getSurfForecastData(spotId);
        double score = round(clamp(
                waveScore(forecast.waveHeightM())
                        + periodScore(forecast.wavePeriodS(), forecast.swellWavePeriodS())
                        + windScore(forecast.windSpeedKmh())
                        + weatherScore(forecast.temperatureC(), forecast.precipitationMm())
        ));

        return new SurfScoreResponse(
                forecast.spotId(),
                forecast.spotName(),
                forecast.dateTime(),
                forecast.waveHeightM(),
                forecast.wavePeriodS(),
                forecast.swellWaveHeightM(),
                forecast.swellWavePeriodS(),
                forecast.windSpeedKmh(),
                forecast.windDirectionDegrees(),
                forecast.windDirectionCompass(),
                forecast.temperatureC(),
                forecast.precipitationMm(),
                score,
                recommendation(score),
                forecast.dataSource()
        );
    }

    private double waveScore(Double waveHeightM) {
        if (waveHeightM < 0.5) {
            return 0.5;
        }
        if (waveHeightM < 0.8) {
            return 1.5;
        }
        if (waveHeightM <= 2.0) {
            return 3.5;
        }
        if (waveHeightM <= 3.0) {
            return 2.5;
        }
        return 1.0;
    }

    private double periodScore(Double wavePeriodS, Double swellWavePeriodS) {
        double effectivePeriod = Math.max(wavePeriodS, swellWavePeriodS);
        if (effectivePeriod < 7) {
            return 0.5;
        }
        if (effectivePeriod < 10) {
            return 1.5;
        }
        if (effectivePeriod < 14) {
            return 2.5;
        }
        return 2.3;
    }

    private double windScore(Double windSpeedKmh) {
        if (windSpeedKmh <= 15) {
            return 2.0;
        }
        if (windSpeedKmh <= 25) {
            return 1.4;
        }
        if (windSpeedKmh <= 35) {
            return 0.7;
        }
        return 0.2;
    }

    private double weatherScore(Double temperatureC, Double precipitationMm) {
        double score = 2.0;
        if (precipitationMm >= 10) {
            score -= 1.5;
        } else if (precipitationMm >= 5) {
            score -= 1.0;
        }
        if (temperatureC < 8) {
            score -= 0.5;
        }
        return Math.max(score, 0.0);
    }

    private String recommendation(double score) {
        if (score >= 8.0) {
            return "Excellent surf conditions";
        }
        if (score >= 6.5) {
            return "Good surf conditions";
        }
        if (score >= 4.5) {
            return "Usable but mixed surf conditions";
        }
        return "Poor surf conditions";
    }

    private double clamp(double score) {
        return Math.max(0.0, Math.min(10.0, score));
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
