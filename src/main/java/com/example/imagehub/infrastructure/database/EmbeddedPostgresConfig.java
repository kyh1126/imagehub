package com.example.imagehub.infrastructure.database;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.url", havingValue = "jdbc:postgresql://localhost:5432/postgres")
public class EmbeddedPostgresConfig {

    @Bean
    public DataSource embeddedPostgresDataSource() throws IOException {
        EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder().setPort(5432).start();
        return embeddedPostgres.getPostgresDatabase();
    }
}
