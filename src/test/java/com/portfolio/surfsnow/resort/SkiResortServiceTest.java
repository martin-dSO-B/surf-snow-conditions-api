package com.portfolio.surfsnow.resort;

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
class SkiResortServiceTest {

    @Mock
    private SkiResortRepository skiResortRepository;

    @InjectMocks
    private SkiResortService skiResortService;

    @Test
    void findAllReturnsMappedSkiResorts() {
        when(skiResortRepository.findAll()).thenReturn(List.of(baqueira(), formigal()));

        List<SkiResortResponse> response = skiResortService.findAll();

        assertThat(response).hasSize(2);
        assertThat(response.getFirst().name()).isEqualTo("Baqueira Beret");
        assertThat(response.getFirst().midAltitude()).isEqualTo(2055);
        assertThat(response.get(1).name()).isEqualTo("Formigal");
    }

    @Test
    void findByIdReturnsMappedSkiResort() {
        when(skiResortRepository.findById(1L)).thenReturn(Optional.of(baqueira()));

        SkiResortResponse response = skiResortService.findById(1L);

        assertThat(response.name()).isEqualTo("Baqueira Beret");
        assertThat(response.country()).isEqualTo("Spain");
        assertThat(response.baseAltitude()).isEqualTo(1500);
        assertThat(response.midAltitude()).isEqualTo(2055);
        assertThat(response.topAltitude()).isEqualTo(2610);
    }

    @Test
    void findByIdThrowsWhenSkiResortDoesNotExist() {
        when(skiResortRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> skiResortService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ski resort not found with id: 99");
    }

    @Test
    void createPersistsAndReturnsSkiResort() {
        SkiResortRequest request = request("Sierra Nevada", 2100, 2700, 3300);
        when(skiResortRepository.save(any(SkiResort.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SkiResortResponse response = skiResortService.create(request);

        assertThat(response.name()).isEqualTo("Sierra Nevada");
        assertThat(response.baseAltitude()).isEqualTo(2100);
        assertThat(response.midAltitude()).isEqualTo(2700);
        assertThat(response.topAltitude()).isEqualTo(3300);
        verify(skiResortRepository).save(any(SkiResort.class));
    }

    @Test
    void createThrowsWhenMidAltitudeIsOutsideRange() {
        SkiResortRequest request = request("Invalid Resort", 1800, 1700, 2500);

        assertThatThrownBy(() -> skiResortService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Middle altitude must be greater than or equal to base altitude and less than or equal to top altitude");
    }

    @Test
    void updateChangesExistingSkiResort() {
        SkiResort skiResort = baqueira();
        SkiResortRequest request = request("Baqueira Updated", 1600, 2100, 2700);
        when(skiResortRepository.findById(1L)).thenReturn(Optional.of(skiResort));

        SkiResortResponse response = skiResortService.update(1L, request);

        assertThat(response.name()).isEqualTo("Baqueira Updated");
        assertThat(response.baseAltitude()).isEqualTo(1600);
        assertThat(response.midAltitude()).isEqualTo(2100);
        assertThat(response.topAltitude()).isEqualTo(2700);
        assertThat(skiResort.getName()).isEqualTo("Baqueira Updated");
    }

    @Test
    void deleteRemovesExistingSkiResort() {
        SkiResort skiResort = baqueira();
        when(skiResortRepository.findById(1L)).thenReturn(Optional.of(skiResort));

        skiResortService.delete(1L);

        verify(skiResortRepository).delete(skiResort);
    }

    private SkiResort baqueira() {
        return new SkiResort(
                "Baqueira Beret",
                "Spain",
                "Val d'Aran",
                42.7,
                0.93,
                1500,
                2055,
                2610
        );
    }

    private SkiResort formigal() {
        return new SkiResort(
                "Formigal",
                "Spain",
                "Aragon",
                42.77,
                -0.36,
                1500,
                1800,
                2250
        );
    }

    private SkiResortRequest request(String name, Integer baseAltitude, Integer midAltitude, Integer topAltitude) {
        return new SkiResortRequest(
                name,
                "Spain",
                "Andalusia",
                37.09,
                -3.4,
                baseAltitude,
                midAltitude,
                topAltitude
        );
    }
}
