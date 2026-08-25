package com.portfolio.surfsnow.resort;

import java.time.OffsetDateTime;

public record SkiResortResponse(
        Long id,
        String name,
        String country,
        String region,
        Double latitude,
        Double longitude,
        Integer baseAltitude,
        Integer topAltitude,
        OffsetDateTime createdAt
) {
}
