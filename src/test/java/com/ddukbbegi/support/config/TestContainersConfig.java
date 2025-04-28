package com.ddukbbegi.support.config;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

//@Testcontainers
@ActiveProfiles("testcontainers")
public abstract class TestContainersConfig {

    // --- 1) 싱글톤 컨테이너 선언 및 수동 시작 ---
    private static final MySQLContainer<?> MYSQL_CONTAINER;
    private static final GenericContainer<?> REDIS_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("test-db")
                .withUsername("test")
                .withPassword("test");
        MYSQL_CONTAINER.start();

        REDIS_CONTAINER = new GenericContainer<>("redis:7.2")
                .withExposedPorts(6379);
        REDIS_CONTAINER.start();
    }

    // --- 2) Spring 프로퍼티 오버라이드 ---
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

}
