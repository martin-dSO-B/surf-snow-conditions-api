package com.portfolio.surfsnow.spot;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/surf-spots")
public class SurfSpotController {

    private final SurfSpotService surfSpotService;

    public SurfSpotController(SurfSpotService surfSpotService) {
        this.surfSpotService = surfSpotService;
    }

    @GetMapping
    public List<SurfSpotResponse> findAll() {
        return surfSpotService.findAll();
    }

    @GetMapping("/{id}")
    public SurfSpotResponse findById(@PathVariable Long id) {
        return surfSpotService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SurfSpotResponse> create(@Valid @RequestBody SurfSpotRequest request) {
        SurfSpotResponse response = surfSpotService.create(request);
        return ResponseEntity
                .created(URI.create("/api/surf-spots/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    public SurfSpotResponse update(@PathVariable Long id, @Valid @RequestBody SurfSpotRequest request) {
        return surfSpotService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        surfSpotService.delete(id);
    }
}
