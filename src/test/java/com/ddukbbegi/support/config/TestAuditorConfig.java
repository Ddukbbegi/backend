package com.ddukbbegi.support.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@TestConfiguration
public class TestAuditorConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> Optional.of(1L);
    }

}
