package com.portfolio.surfsnow.forecast;

public record SkiElevationForecastResponse(
        ElevationType elevationType,
        Integer elevation,
        String dateTime,
        Double temperatureC,
        Double apparentTemperatureC,
        Double windSpeedKmh,
        Integer windDirectionDegrees,
        String windDirectionCompass,
        Double precipitationMm,
        Integer precipitationProbability,
        Double snowfallCm,
        Double snowDepthM,
        Double freezingLevelHeightM
) {
}
