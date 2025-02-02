package com.example.imagehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class ImageHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageHubApplication.class, args);
    }
}
