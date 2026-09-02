package com.portfolio.surfsnow.forecast;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forecasts")
@Tag(name = "Forecasts")
public class ForecastController {

    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/surf/{spotId}")
    @Operation(summary = "Get current surf forecast")
    public SurfForecastResponse getSurfForecast(@PathVariable Long spotId) {
        return forecastService.getSurfForecast(spotId);
    }

    @GetMapping("/ski/{resortId}")
    @Operation(summary = "Get current ski forecast")
    public SkiForecastResponse getSkiForecast(@PathVariable Long resortId) {
        return forecastService.getSkiForecast(resortId);
    }
}
