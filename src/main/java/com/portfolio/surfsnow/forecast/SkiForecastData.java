package com.portfolio.surfsnow.forecast;

public record SkiForecastData(
        Long resortId,
        String resortName,
        Integer baseAltitude,
        Integer midAltitude,
        Integer topAltitude,
        SkiElevationForecastResponse base,
        SkiElevationForecastResponse mid,
        SkiElevationForecastResponse top,
        DataSource dataSource
) {
    SkiForecastResponse toResponse() {
        return new SkiForecastResponse(resortId, resortName, base, mid, top, dataSource);
    }
}
