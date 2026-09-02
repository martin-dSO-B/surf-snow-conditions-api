package com.portfolio.surfsnow.scoring;

import com.portfolio.surfsnow.forecast.DataSource;
import com.portfolio.surfsnow.forecast.SkiElevationForecastResponse;

public record SkiScoreResponse(
        Long resortId,
        String resortName,
        Integer baseAltitude,
        Integer midAltitude,
        Integer topAltitude,
        SkiElevationForecastResponse baseForecast,
        SkiElevationForecastResponse midForecast,
        SkiElevationForecastResponse topForecast,
        Double skiScore,
        String recommendation,
        DataSource dataSource
) {
}
