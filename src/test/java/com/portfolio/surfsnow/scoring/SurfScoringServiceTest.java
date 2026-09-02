package com.portfolio.surfsnow.scoring;

import com.portfolio.surfsnow.forecast.DataSource;
import com.portfolio.surfsnow.forecast.ForecastService;
import com.portfolio.surfsnow.forecast.SurfForecastData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurfScoringServiceTest {

    @Mock
    private ForecastService forecastService;

    @InjectMocks
    private SurfScoringService surfScoringService;

    @Test
    void returnsExcellentScoreForCleanSurfConditions() {
        when(forecastService.getSurfForecastData(1L)).thenReturn(new SurfForecastData(
                1L,
                "Mundaka",
                "2026-09-02T12:00",
                18.0,
                10.0,
                180,
                "S",
                0.0,
                1.2,
                11.0,
                1.1,
                12.0,
                315,
                "NW",
                DataSource.OPEN_METEO
        ));

        SurfScoreResponse response = surfScoringService.getSurfScore(1L);

        assertThat(response.surfScore()).isEqualTo(10.0);
        assertThat(response.recommendation()).isEqualTo("Excellent surf conditions");
        assertThat(response.dataSource()).isEqualTo(DataSource.OPEN_METEO);
    }
}
