package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.TokenProviderPort;
import com.example.imagehub.application.port.out.UserPort;
import com.example.imagehub.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserPort userPort;
    private TokenProviderPort tokenProviderPort;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        userPort = mock(UserPort.class);
        tokenProviderPort = mock(TokenProviderPort.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationManager = mock(AuthenticationManager.class);
        authService = new AuthService(userPort, tokenProviderPort, passwordEncoder, authenticationManager);
    }

    @Test
    void signUpShouldSaveUser() {
        UserModel userModel = new UserModel("test1", "testUser", "password123", "USER");

        authService.signUp(userModel);

        verify(userPort).create(any(UserModel.class));
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreValid() {
        String userId = "test1";
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProviderPort.generateToken(authentication)).thenReturn("mockedToken");

        String token = authService.login(userId, rawPassword);

        assertEquals("mockedToken", token);
    }

    @Test
    void loginShouldThrowExceptionWhenCredentialsAreInvalid() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login("test1", "wrongPassword"));
    }
}
