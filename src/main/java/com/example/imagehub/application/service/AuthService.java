package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.AuthUseCase;
import com.example.imagehub.application.port.out.TokenProviderPort;
import com.example.imagehub.application.port.out.UserRepositoryPort;
import com.example.imagehub.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {
    private final UserRepositoryPort userRepository;
    private final TokenProviderPort tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public String signUp(UserModel userModel) {
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(userModel);
        return "User registered successfully";
    }

    @Override
    public String login(String userId, String password) {
        var unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(userId, password);
        var authentication = authenticationManager.authenticate(unauthenticatedToken);
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }

        return tokenProvider.generateToken(authentication);
    }
}
