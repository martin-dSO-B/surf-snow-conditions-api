package com.portfolio.surfsnow.scoring;

import com.portfolio.surfsnow.forecast.DataSource;
import com.portfolio.surfsnow.forecast.ElevationType;
import com.portfolio.surfsnow.forecast.ForecastService;
import com.portfolio.surfsnow.forecast.SkiElevationForecastResponse;
import com.portfolio.surfsnow.forecast.SkiForecastData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkiScoringServiceTest {

    @Mock
    private ForecastService forecastService;

    @InjectMocks
    private SkiScoringService skiScoringService;

    @Test
    void returnsExcellentScoreForColdSnowyConditionsAcrossElevations() {
        SkiForecastData forecast = new SkiForecastData(
                1L,
                "Baqueira Beret",
                1500,
                2055,
                2610,
                elevation(ElevationType.BASE, 1500),
                elevation(ElevationType.MID, 2055),
                elevation(ElevationType.TOP, 2610),
                DataSource.OPEN_METEO
        );
        when(forecastService.getSkiForecastData(1L)).thenReturn(forecast);

        SkiScoreResponse response = skiScoringService.getSkiScore(1L);

        assertThat(response.skiScore()).isEqualTo(9.2);
        assertThat(response.recommendation()).isEqualTo("Excellent ski conditions");
        assertThat(response.baseAltitude()).isEqualTo(1500);
        assertThat(response.midAltitude()).isEqualTo(2055);
        assertThat(response.topAltitude()).isEqualTo(2610);
    }

    @Test
    void returnsPoorScoreForWarmRainyWindyConditionsAcrossElevations() {
        SkiForecastData forecast = new SkiForecastData(
                1L,
                "Baqueira Beret",
                1500,
                2055,
                2610,
                poorElevation(ElevationType.BASE, 1500),
                poorElevation(ElevationType.MID, 2055),
                poorElevation(ElevationType.TOP, 2610),
                DataSource.MOCK_FALLBACK
        );
        when(forecastService.getSkiForecastData(1L)).thenReturn(forecast);

        SkiScoreResponse response = skiScoringService.getSkiScore(1L);

        assertThat(response.skiScore()).isEqualTo(2.3);
        assertThat(response.recommendation()).isEqualTo("Poor ski conditions");
        assertThat(response.dataSource()).isEqualTo(DataSource.MOCK_FALLBACK);
    }

    private SkiElevationForecastResponse elevation(ElevationType elevationType, Integer elevation) {
        return new SkiElevationForecastResponse(
                elevationType,
                elevation,
                "2026-09-02T12:00",
                -2.0,
                -4.0,
                10.0,
                45,
                "NE",
                0.0,
                20,
                0.0,
                0.4,
                1200.0
        );
    }

    private SkiElevationForecastResponse poorElevation(ElevationType elevationType, Integer elevation) {
        return new SkiElevationForecastResponse(
                elevationType,
                elevation,
                "2026-09-02T12:00",
                7.0,
                4.0,
                55.0,
                210,
                "SW",
                8.0,
                90,
                0.0,
                0.0,
                3400.0
        );
    }
}
