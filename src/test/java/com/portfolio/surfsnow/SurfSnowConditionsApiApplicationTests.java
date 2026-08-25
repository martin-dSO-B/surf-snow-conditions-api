package com.portfolio.surfsnow;

import com.portfolio.surfsnow.resort.SkiResortRepository;
import com.portfolio.surfsnow.spot.SurfSpotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class SurfSnowConditionsApiApplicationTests {

    @MockBean
    private SurfSpotRepository surfSpotRepository;

    @MockBean
    private SkiResortRepository skiResortRepository;

    @Test
    void contextLoads() {
    }
}
