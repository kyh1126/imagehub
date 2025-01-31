package com.example.imagehub.infrastructure.database;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.example.imagehub.adapter.out.persistence")
@EnableJpaRepositories(basePackages = "com.example.imagehub.adapter.out.persistence")
public class JpaConfig {
}
