package com.portfolio.surfsnow.forecast;

public record SkiForecastResponse(
        Long resortId,
        String resortName,
        SkiElevationForecastResponse base,
        SkiElevationForecastResponse mid,
        SkiElevationForecastResponse top,
        DataSource dataSource
) {
}
