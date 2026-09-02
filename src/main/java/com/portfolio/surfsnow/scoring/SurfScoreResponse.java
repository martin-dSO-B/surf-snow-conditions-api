package com.portfolio.surfsnow.scoring;

import com.portfolio.surfsnow.forecast.DataSource;

public record SurfScoreResponse(
        Long spotId,
        String spotName,
        String dateTime,
        Double waveHeightM,
        Double wavePeriodS,
        Double swellWaveHeightM,
        Double swellWavePeriodS,
        Double windSpeedKmh,
        Integer windDirectionDegrees,
        String windDirectionCompass,
        Double temperatureC,
        Double precipitationMm,
        Double surfScore,
        String recommendation,
        DataSource dataSource
) {
}
