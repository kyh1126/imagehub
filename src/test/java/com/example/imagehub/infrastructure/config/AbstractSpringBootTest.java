package com.example.imagehub.infrastructure.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractSpringBootTest {

    private static EmbeddedPostgres embeddedPostgres;

    // Spring Context 로딩 이전에 EmbeddedPostgres 초기화
    static {
        try {
            embeddedPostgres = EmbeddedPostgres.builder().setPort(0).start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start EmbeddedPostgres", e);
        }
    }

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        // EmbeddedPostgres의 포트를 사용하여 DataSource 설정
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:" + embeddedPostgres.getPort() + "/postgres");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
    }

    @AfterAll
    static void tearDown() throws IOException {
        if (embeddedPostgres != null) {
            embeddedPostgres.close();
        }
    }

    @BeforeAll
    void setUp() {
        System.out.println("AbstractSpringBootTest - BeforeAll");
    }
}
