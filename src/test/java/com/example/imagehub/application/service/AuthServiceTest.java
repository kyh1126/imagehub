package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.TokenProviderPort;
import com.example.imagehub.application.port.out.UserRepositoryPort;
import com.example.imagehub.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserRepositoryPort userRepositoryPort;
    private TokenProviderPort tokenProviderPort;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        tokenProviderPort = mock(TokenProviderPort.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepositoryPort, tokenProviderPort, passwordEncoder);
    }

    @Test
    void signUpShouldSaveUser() {
        UserModel userModel = new UserModel("test1", "testUser", "password123", "USER");

        authService.signUp(userModel);

        verify(userRepositoryPort).save(any(UserModel.class));
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreValid() {
        UserModel userModel = new UserModel("test1", "testUser", passwordEncoder.encode("password123"), "USER");
        when(userRepositoryPort.findByUserId("test1")).thenReturn(Optional.of(userModel));
        when(tokenProviderPort.generateToken("test1", "USER")).thenReturn("mockedToken");

        String token = authService.login("test1", "password123");

        assertEquals("mockedToken", token);
    }

    @Test
    void loginShouldThrowExceptionWhenCredentialsAreInvalid() {
        when(userRepositoryPort.findByUserId("test1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login("test1", "wrongPassword"));
    }
}
