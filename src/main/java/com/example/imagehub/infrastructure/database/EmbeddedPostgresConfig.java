package com.example.imagehub.infrastructure.database;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;

@Slf4j
@Configuration
@Profile("!test")
public class EmbeddedPostgresConfig {

    @Value("${embedded.postgres.port}")
    private int port;

    private EmbeddedPostgres embeddedPostgres;

    @Bean
    public DataSource embeddedPostgresDataSource() throws IOException {
        embeddedPostgres = EmbeddedPostgres.builder().setPort(port).start();
        return embeddedPostgres.getPostgresDatabase();
    }

    @PreDestroy
    public void stopEmbeddedPostgres() {
        if (embeddedPostgres != null) {
            try {
                embeddedPostgres.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
