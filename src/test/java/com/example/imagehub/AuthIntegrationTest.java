package com.example.imagehub;

import com.example.imagehub.infrastructure.config.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthIntegrationTest extends AbstractSpringBootTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void signUpIntegrationTest() {
        String requestBody = "{\"userId\": \"test1\", \"username\": \"testUser\", \"password\": \"password123\"," +
                " \"role\": \"USER\"}";

        // Content-Type을 JSON으로 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signup", requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("User registered successfully"));
    }

    @Test
    void loginIntegrationTest() {
        String signUpRequestBody = """
                    {
                        "userId": "test2",
                        "username": "testUser",
                        "password": "password123",
                        "role": "USER"
                    }
                """;

        // Content-Type을 JSON으로 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> signUpRequest = new HttpEntity<>(signUpRequestBody, headers);
        var status = restTemplate.postForEntity("/auth/signup", signUpRequest, String.class).getStatusCode();

        assertEquals(HttpStatus.OK, status);

        String signInRequestBody = "{\"userId\": \"test2\", \"password\": \"password123\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(signInRequestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("token"));
    }
}
