package com.example.imagehub.infrastructure.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@Profile("test")
public class TestEmbeddedPostgresConfig {

    @Value("${embedded.postgres.port}")
    private int port;

    @Bean(destroyMethod = "close") // 테스트 종료 시 자동으로 종료
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        return EmbeddedPostgres.builder().setPort(port).start();
    }

    @Bean
    public DataSource dataSource(EmbeddedPostgres embeddedPostgres) {
        return embeddedPostgres.getPostgresDatabase();
    }
}
