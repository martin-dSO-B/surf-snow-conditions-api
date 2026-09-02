package com.portfolio.surfsnow.forecast;

public record SurfForecastResponse(
        Long spotId,
        String spotName,
        String dateTime,
        Double temperatureC,
        Double windSpeedKmh,
        Integer windDirectionDegrees,
        String windDirectionCompass,
        Double precipitationMm,
        Double waveHeightM,
        Double wavePeriodS,
        Double swellWaveHeightM,
        Double swellWavePeriodS,
        Integer swellWaveDirectionDegrees,
        String swellWaveDirectionCompass,
        DataSource dataSource
) {
}
