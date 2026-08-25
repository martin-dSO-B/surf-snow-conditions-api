package com.portfolio.surfsnow.resort;

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
@RequestMapping("/api/ski-resorts")
public class SkiResortController {

    private final SkiResortService skiResortService;

    public SkiResortController(SkiResortService skiResortService) {
        this.skiResortService = skiResortService;
    }

    @GetMapping
    public List<SkiResortResponse> findAll() {
        return skiResortService.findAll();
    }

    @GetMapping("/{id}")
    public SkiResortResponse findById(@PathVariable Long id) {
        return skiResortService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SkiResortResponse> create(@Valid @RequestBody SkiResortRequest request) {
        SkiResortResponse response = skiResortService.create(request);
        return ResponseEntity
                .created(URI.create("/api/ski-resorts/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    public SkiResortResponse update(@PathVariable Long id, @Valid @RequestBody SkiResortRequest request) {
        return skiResortService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        skiResortService.delete(id);
    }
}
