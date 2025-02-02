package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.SignInCommand;
import com.example.imagehub.application.port.in.SignUpCommand;
import com.example.imagehub.application.port.out.CreateUserPort;
import com.example.imagehub.application.port.out.TokenProviderPort;
import com.example.imagehub.domain.User;
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
    private CreateUserPort createUserPort;
    private TokenProviderPort tokenProviderPort;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        createUserPort = mock(CreateUserPort.class);
        tokenProviderPort = mock(TokenProviderPort.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationManager = mock(AuthenticationManager.class);
        authService = new AuthService(createUserPort, tokenProviderPort, passwordEncoder, authenticationManager);
    }

    @Test
    void signUpShouldSaveUser() {
        SignUpCommand signUpCommand = new SignUpCommand("test1", "testUser", "password123", "USER");

        authService.signUp(signUpCommand);

        verify(createUserPort).create(any(User.class));
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreValid() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProviderPort.generateToken(authentication)).thenReturn("mockedToken");

        SignInCommand signInCommand = new SignInCommand("test1", "password123");

        String token = authService.login(signInCommand);

        assertEquals("mockedToken", token);
    }

    @Test
    void loginShouldThrowExceptionWhenCredentialsAreInvalid() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        SignInCommand signInCommand = new SignInCommand("test1", "wrongPassword");

        assertThrows(RuntimeException.class, () -> authService.login(signInCommand));
    }
}
