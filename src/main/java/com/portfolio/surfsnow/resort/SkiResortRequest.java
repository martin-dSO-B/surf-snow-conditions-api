package com.portfolio.surfsnow.resort;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SkiResortRequest(
        @NotBlank
        @Size(max = 120)
        String name,

        @NotBlank
        @Size(max = 80)
        String country,

        @NotBlank
        @Size(max = 120)
        String region,

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double latitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double longitude,

        @NotNull
        @Positive
        Integer baseAltitude,

        @NotNull
        @Positive
        Integer topAltitude
) {
}
