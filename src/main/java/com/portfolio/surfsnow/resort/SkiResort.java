package com.portfolio.surfsnow.resort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "ski_resorts")
public class SkiResort {

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

    @Column(name = "base_altitude", nullable = false)
    private Integer baseAltitude;

    @Column(name = "mid_altitude", nullable = false)
    private Integer midAltitude;

    @Column(name = "top_altitude", nullable = false)
    private Integer topAltitude;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected SkiResort() {
    }

    public SkiResort(
            String name,
            String country,
            String region,
            Double latitude,
            Double longitude,
            Integer baseAltitude,
            Integer midAltitude,
            Integer topAltitude
    ) {
        this.name = name;
        this.country = country;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.baseAltitude = baseAltitude;
        this.midAltitude = midAltitude;
        this.topAltitude = topAltitude;
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

    public Integer getBaseAltitude() {
        return baseAltitude;
    }

    public void setBaseAltitude(Integer baseAltitude) {
        this.baseAltitude = baseAltitude;
    }

    public Integer getMidAltitude() {
        return midAltitude;
    }

    public void setMidAltitude(Integer midAltitude) {
        this.midAltitude = midAltitude;
    }

    public Integer getTopAltitude() {
        return topAltitude;
    }

    public void setTopAltitude(Integer topAltitude) {
        this.topAltitude = topAltitude;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
