package com.portfolio.surfsnow.forecast;

public record SurfForecastData(
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
    SurfForecastResponse toResponse() {
        return new SurfForecastResponse(
                spotId,
                spotName,
                dateTime,
                temperatureC,
                windSpeedKmh,
                windDirectionDegrees,
                windDirectionCompass,
                precipitationMm,
                waveHeightM,
                wavePeriodS,
                swellWaveHeightM,
                swellWavePeriodS,
                swellWaveDirectionDegrees,
                swellWaveDirectionCompass,
                dataSource
        );
    }
}
