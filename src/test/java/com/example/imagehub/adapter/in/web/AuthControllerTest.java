package com.example.imagehub.adapter.in.web;

import com.example.imagehub.adapter.in.AuthController;
import com.example.imagehub.adapter.in.CategoryController;
import com.example.imagehub.adapter.in.ImageController;
import com.example.imagehub.application.port.in.*;
import com.example.imagehub.application.port.out.ImageResponse;
import com.example.imagehub.domain.Image;
import com.example.imagehub.infrastructure.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class) // Test 전용 Security 설정
@WebMvcTest({AuthController.class, ImageController.class, CategoryController.class})
class AuthControllerTest {

    private final String validJwtToken = "Bearer valid.jwt.token";
    private final String invalidJwtToken = "Bearer invalid.jwt.token";
    private final PageRequest pageRequest = PageRequest.ofSize(10);
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtDecoder jwtDecoder;
    @MockitoBean
    private AuthUseCase authUseCase;
    @MockitoBean
    private ImageUseCase imageUseCase;
    @MockitoBean
    private CategoryUseCase categoryUseCase;

    @Test
    void signUpShouldReturnSuccessMessage() throws Exception {
        var requestJson = """
                    {
                        "userId": "test1",
                        "username": "testUser",
                        "password": "password123",
                        "role": "USER"
                    }
                """;

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void loginShouldReturnToken() throws Exception {
        SignInCommand signInCommand = new SignInCommand("test1", "password123");
        when(authUseCase.login(signInCommand)).thenReturn("mockedToken");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"test1\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedToken"));
    }

    @Test
    void testGetImages_WithValidToken() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[10]  // 빈 바이트 배열 (10바이트)
        );
        UploadImageCommand uploadImageCommand = new UploadImageCommand(file, "Test Description", List.of("PERSON"));
        Image image = Image.of(uploadImageCommand, "test.jpg", "uploads", "thumbnails");
        var images = List.of(ImageResponse.from(image));

        when(imageUseCase.getImages(pageRequest)).thenReturn(images);

        Instant now = Instant.now();
        Jwt mockJwt = new Jwt(
                validJwtToken, now, now.plusSeconds(3600),
                Map.of("kid", "imagehub-key", "alg", "HS256"), // Header
                Map.of("sub", "test1", "roles", "ROLE_USER") // Claims
        );
        when(jwtDecoder.decode("valid.jwt.token")).thenReturn(mockJwt);

        mockMvc.perform(get("/images")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("test.jpg"));
    }

    @Test
    void testGetImages_WithInvalidToken() throws Exception {
        Instant now = Instant.now();
        Jwt mockJwt = new Jwt(
                validJwtToken, now, now.plusSeconds(3600),
                Map.of("kid", "imagehub-key", "alg", "HS256"), // Header
                Map.of("sub", "test1", "roles", "ROLE_USER") // Claims
        );
        when(jwtDecoder.decode("valid.jwt.token")).thenReturn(mockJwt);

        mockMvc.perform(get("/images")
                        .header("Authorization", invalidJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAddCategory_WithAdminRole_Success() throws Exception {
        Instant now = Instant.now();
        Jwt mockJwt = new Jwt(
                validJwtToken, now, now.plusSeconds(3600),
                Map.of("kid", "imagehub-key", "alg", "HS256"), // Header
                Map.of("sub", "test1", "roles", "ROLE_ADMIN") // Claims
                // TODO:
                //  1. 테스트 코드 상 Claims 에 반영 필요
                //    - "exp" -> {Instant@17767} "2025-02-01T19:46:43Z", "iat" -> {Instant@17766} "2025-02-01T18:46:43Z"
                //  2. 만료 테스트 코드 추가
        );
        when(jwtDecoder.decode("valid.jwt.token")).thenReturn(mockJwt);

        mockMvc.perform(post("/categories")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"LANDSCAPE2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category added successfully"));
    }

    @Test
    void testAddCategory_WithUserRole_Fail() throws Exception {
        Instant now = Instant.now();
        Jwt mockJwt = new Jwt(
                validJwtToken, now, now.plusSeconds(3600),
                Map.of("kid", "imagehub-key", "alg", "HS256"), // Header
                Map.of("sub", "test1", "roles", "ROLE_USER") // Claims
        );
        when(jwtDecoder.decode("valid.jwt.token")).thenReturn(mockJwt);

        mockMvc.perform(post("/categories")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"LANDSCAPE2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category added successfully"));
    }
}
