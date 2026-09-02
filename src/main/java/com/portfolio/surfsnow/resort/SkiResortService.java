package com.portfolio.surfsnow.resort;

import com.portfolio.surfsnow.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SkiResortService {

    private final SkiResortRepository skiResortRepository;

    public SkiResortService(SkiResortRepository skiResortRepository) {
        this.skiResortRepository = skiResortRepository;
    }

    public List<SkiResortResponse> findAll() {
        return skiResortRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SkiResortResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public SkiResortResponse create(SkiResortRequest request) {
        validateAltitudeRange(request);

        SkiResort skiResort = new SkiResort(
                request.name(),
                request.country(),
                request.region(),
                request.latitude(),
                request.longitude(),
                request.baseAltitude(),
                request.midAltitude(),
                request.topAltitude()
        );

        return toResponse(skiResortRepository.save(skiResort));
    }

    @Transactional
    public SkiResortResponse update(Long id, SkiResortRequest request) {
        validateAltitudeRange(request);

        SkiResort skiResort = findEntityById(id);
        skiResort.setName(request.name());
        skiResort.setCountry(request.country());
        skiResort.setRegion(request.region());
        skiResort.setLatitude(request.latitude());
        skiResort.setLongitude(request.longitude());
        skiResort.setBaseAltitude(request.baseAltitude());
        skiResort.setMidAltitude(request.midAltitude());
        skiResort.setTopAltitude(request.topAltitude());

        return toResponse(skiResort);
    }

    @Transactional
    public void delete(Long id) {
        SkiResort skiResort = findEntityById(id);
        skiResortRepository.delete(skiResort);
    }

    private SkiResort findEntityById(Long id) {
        return skiResortRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ski resort not found with id: " + id));
    }

    private void validateAltitudeRange(SkiResortRequest request) {
        if (request.midAltitude() < request.baseAltitude() || request.midAltitude() > request.topAltitude()) {
            throw new IllegalArgumentException("Middle altitude must be greater than or equal to base altitude and less than or equal to top altitude");
        }
    }

    private SkiResortResponse toResponse(SkiResort skiResort) {
        return new SkiResortResponse(
                skiResort.getId(),
                skiResort.getName(),
                skiResort.getCountry(),
                skiResort.getRegion(),
                skiResort.getLatitude(),
                skiResort.getLongitude(),
                skiResort.getBaseAltitude(),
                skiResort.getMidAltitude(),
                skiResort.getTopAltitude(),
                skiResort.getCreatedAt()
        );
    }
}
