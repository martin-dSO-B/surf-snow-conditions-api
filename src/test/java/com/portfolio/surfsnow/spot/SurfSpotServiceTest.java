package com.portfolio.surfsnow.spot;

import com.portfolio.surfsnow.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurfSpotServiceTest {

    @Mock
    private SurfSpotRepository surfSpotRepository;

    @InjectMocks
    private SurfSpotService surfSpotService;

    @Test
    void findAllReturnsMappedSurfSpots() {
        when(surfSpotRepository.findAll()).thenReturn(List.of(mundaka(), nazare()));

        List<SurfSpotResponse> response = surfSpotService.findAll();

        assertThat(response).hasSize(2);
        assertThat(response.getFirst().name()).isEqualTo("Mundaka");
        assertThat(response.getFirst().difficulty()).isEqualTo(SurfSpot.Difficulty.ADVANCED);
        assertThat(response.get(1).name()).isEqualTo("Nazare");
    }

    @Test
    void findByIdReturnsMappedSurfSpot() {
        when(surfSpotRepository.findById(1L)).thenReturn(Optional.of(mundaka()));

        SurfSpotResponse response = surfSpotService.findById(1L);

        assertThat(response.name()).isEqualTo("Mundaka");
        assertThat(response.country()).isEqualTo("Spain");
        assertThat(response.region()).isEqualTo("Basque Country");
        assertThat(response.bestWindDirection()).isEqualTo("S");
        assertThat(response.bestSwellDirection()).isEqualTo("NW");
    }

    @Test
    void findByIdThrowsWhenSurfSpotDoesNotExist() {
        when(surfSpotRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> surfSpotService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Surf spot not found with id: 99");
    }

    @Test
    void createPersistsAndReturnsSurfSpot() {
        SurfSpotRequest request = request("Supertubos", SurfSpot.Difficulty.INTERMEDIATE);
        when(surfSpotRepository.save(any(SurfSpot.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SurfSpotResponse response = surfSpotService.create(request);

        assertThat(response.name()).isEqualTo("Supertubos");
        assertThat(response.country()).isEqualTo("Portugal");
        assertThat(response.difficulty()).isEqualTo(SurfSpot.Difficulty.INTERMEDIATE);
        verify(surfSpotRepository).save(any(SurfSpot.class));
    }

    @Test
    void updateChangesExistingSurfSpotWithoutCallingSave() {
        SurfSpot surfSpot = mundaka();
        SurfSpotRequest request = request("Mundaka Updated", SurfSpot.Difficulty.EXPERT);
        when(surfSpotRepository.findById(1L)).thenReturn(Optional.of(surfSpot));

        SurfSpotResponse response = surfSpotService.update(1L, request);

        assertThat(response.name()).isEqualTo("Mundaka Updated");
        assertThat(response.difficulty()).isEqualTo(SurfSpot.Difficulty.EXPERT);
        assertThat(surfSpot.getName()).isEqualTo("Mundaka Updated");
    }

    @Test
    void deleteRemovesExistingSurfSpot() {
        SurfSpot surfSpot = mundaka();
        when(surfSpotRepository.findById(1L)).thenReturn(Optional.of(surfSpot));

        surfSpotService.delete(1L);

        verify(surfSpotRepository).delete(surfSpot);
    }

    private SurfSpot mundaka() {
        return new SurfSpot(
                "Mundaka",
                "Spain",
                "Basque Country",
                43.407,
                -2.698,
                SurfSpot.Difficulty.ADVANCED,
                "S",
                "NW"
        );
    }

    private SurfSpot nazare() {
        return new SurfSpot(
                "Nazare",
                "Portugal",
                "Leiria",
                39.602,
                -9.071,
                SurfSpot.Difficulty.EXPERT,
                "E",
                "W"
        );
    }

    private SurfSpotRequest request(String name, SurfSpot.Difficulty difficulty) {
        return new SurfSpotRequest(
                name,
                "Portugal",
                "Peniche",
                39.35,
                -9.37,
                difficulty,
                "E",
                "W"
        );
    }
}
