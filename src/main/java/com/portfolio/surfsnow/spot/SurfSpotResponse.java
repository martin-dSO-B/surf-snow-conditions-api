package com.portfolio.surfsnow.spot;

import java.time.OffsetDateTime;

public record SurfSpotResponse(
        Long id,
        String name,
        String country,
        String region,
        Double latitude,
        Double longitude,
        SurfSpot.Difficulty difficulty,
        String bestWindDirection,
        String bestSwellDirection,
        OffsetDateTime createdAt
) {
}
