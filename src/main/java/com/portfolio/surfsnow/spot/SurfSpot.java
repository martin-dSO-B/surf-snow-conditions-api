package com.portfolio.surfsnow.spot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "surf_spots")
public class SurfSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 80)
    private String country;

    @Column(nullable = false, length = 120)
    private String region;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Difficulty difficulty;

    @Column(name = "best_wind_direction", length = 40)
    private String bestWindDirection;

    @Column(name = "best_swell_direction", length = 40)
    private String bestSwellDirection;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected SurfSpot() {
    }

    public SurfSpot(
            String name,
            String country,
            String region,
            Double latitude,
            Double longitude,
            Difficulty difficulty,
            String bestWindDirection,
            String bestSwellDirection
    ) {
        this.name = name;
        this.country = country;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.difficulty = difficulty;
        this.bestWindDirection = bestWindDirection;
        this.bestSwellDirection = bestSwellDirection;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getBestWindDirection() {
        return bestWindDirection;
    }

    public void setBestWindDirection(String bestWindDirection) {
        this.bestWindDirection = bestWindDirection;
    }

    public String getBestSwellDirection() {
        return bestSwellDirection;
    }

    public void setBestSwellDirection(String bestSwellDirection) {
        this.bestSwellDirection = bestSwellDirection;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public enum Difficulty {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }
}
