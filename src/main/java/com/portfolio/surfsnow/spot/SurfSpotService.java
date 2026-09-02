package com.portfolio.surfsnow.spot;

import com.portfolio.surfsnow.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SurfSpotService {

    private final SurfSpotRepository surfSpotRepository;

    public SurfSpotService(SurfSpotRepository surfSpotRepository) {
        this.surfSpotRepository = surfSpotRepository;
    }

    public List<SurfSpotResponse> findAll() {
        return surfSpotRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SurfSpotResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public SurfSpotResponse create(SurfSpotRequest request) {
        SurfSpot surfSpot = new SurfSpot(
                request.name(),
                request.country(),
                request.region(),
                request.latitude(),
                request.longitude(),
                request.difficulty(),
                request.bestWindDirection(),
                request.bestSwellDirection()
        );

        return toResponse(surfSpotRepository.save(surfSpot));
    }

    @Transactional
    public SurfSpotResponse update(Long id, SurfSpotRequest request) {
        SurfSpot surfSpot = findEntityById(id);

        surfSpot.setName(request.name());
        surfSpot.setCountry(request.country());
        surfSpot.setRegion(request.region());
        surfSpot.setLatitude(request.latitude());
        surfSpot.setLongitude(request.longitude());
        surfSpot.setDifficulty(request.difficulty());
        surfSpot.setBestWindDirection(request.bestWindDirection());
        surfSpot.setBestSwellDirection(request.bestSwellDirection());

        return toResponse(surfSpot);
    }

    @Transactional
    public void delete(Long id) {
        SurfSpot surfSpot = findEntityById(id);
        surfSpotRepository.delete(surfSpot);
    }

    private SurfSpot findEntityById(Long id) {
        return surfSpotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surf spot not found with id: " + id));
    }

    private SurfSpotResponse toResponse(SurfSpot surfSpot) {
        return new SurfSpotResponse(
                surfSpot.getId(),
                surfSpot.getName(),
                surfSpot.getCountry(),
                surfSpot.getRegion(),
                surfSpot.getLatitude(),
                surfSpot.getLongitude(),
                surfSpot.getDifficulty(),
                surfSpot.getBestWindDirection(),
                surfSpot.getBestSwellDirection(),
                surfSpot.getCreatedAt()
        );
    }
}
