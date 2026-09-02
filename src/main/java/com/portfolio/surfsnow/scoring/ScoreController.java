package com.portfolio.surfsnow.scoring;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scores")
@Tag(name = "Scores")
public class ScoreController {

    private final SurfScoringService surfScoringService;
    private final SkiScoringService skiScoringService;

    public ScoreController(SurfScoringService surfScoringService, SkiScoringService skiScoringService) {
        this.surfScoringService = surfScoringService;
        this.skiScoringService = skiScoringService;
    }

    @GetMapping("/surf/{spotId}")
    @Operation(summary = "Get current surf condition score")
    public SurfScoreResponse getSurfScore(@PathVariable Long spotId) {
        return surfScoringService.getSurfScore(spotId);
    }

    @GetMapping("/ski/{resortId}")
    @Operation(summary = "Get current ski condition score")
    public SkiScoreResponse getSkiScore(@PathVariable Long resortId) {
        return skiScoringService.getSkiScore(resortId);
    }
}
