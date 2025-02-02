package com.example.imagehub.infrastructure.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication")) // API 호출 시 JWT 토큰 적용
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication", securityScheme())); // JWT 보안 스키마 등록
    }

    private List<Server> getServers() {
        String baseUrl = "http://localhost:8080";// http://localhost:8080/swagger-ui/index.html
        String description = "for local";

        return List.of(new Server().url(baseUrl).description(description));
    }

    private Info apiInfo() {
        return new Info()
                .title("Image Hub API document")
                .version("1.0.0")
                .description("WIP version");
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("""
                        ex> -H 'Authorization: Bearer eyJraWQiOiJpbWFnZWh1Yi1rZXkiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJreWgxMTI...'
                        \n-> Value: eyJraWQiOiJpbWFnZWh1Yi1rZXkiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJreWgxMTI...
                        """)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER);
    }
}
