package com.portfolio.surfsnow.forecast;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "open-meteo")
public record OpenMeteoProperties(
        String weatherBaseUrl,
        String marineBaseUrl,
        boolean fallbackEnabled
) {
}
