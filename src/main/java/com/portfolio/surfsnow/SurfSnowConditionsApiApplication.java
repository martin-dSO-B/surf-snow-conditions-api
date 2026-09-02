package com.portfolio.surfsnow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SurfSnowConditionsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurfSnowConditionsApiApplication.class, args);
    }
}
