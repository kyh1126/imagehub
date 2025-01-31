package com.example.imagehub.infrastructure.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(getServers())
                .info(apiInfo());
    }

    private List<Server> getServers() {
        String baseUrl;
        String description;

        baseUrl = "http://localhost:8080";
        description = "for local testing";

        return List.of(new Server().url(baseUrl).description(description));
    }

    private Info apiInfo() {
        return new Info()
                .title("Image Hub API document")
                .version("1.0.0")
                .description("WIP version");
    }
}
