package com.example.imagehub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void signUpIntegrationTest() {
        String requestBody = "{\"userId\": \"test1\", \"username\": \"testUser\", \"password\": \"password123\"," +
                " \"role\": \"USER\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signup", requestBody, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("User registered successfully"));
    }

    @Test
    void loginIntegrationTest() {
        String requestBody = "{\"userId\": \"test1\", \"password\": \"password123\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", requestBody, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("token"));
    }
}
