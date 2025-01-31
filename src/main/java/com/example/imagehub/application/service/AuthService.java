package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.AuthUseCase;
import com.example.imagehub.application.port.out.TokenProviderPort;
import com.example.imagehub.application.port.out.UserRepositoryPort;
import com.example.imagehub.domain.model.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {
    private final UserRepositoryPort userRepository;
    private final TokenProviderPort tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryPort userRepository, TokenProviderPort tokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String signUp(UserModel userModel) {
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(userModel);
        return "User registered successfully";
    }

    @Override
    public String login(String userId, String password) {
        UserModel userModel = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, userModel.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return tokenProvider.generateToken(userId, userModel.getRole());
    }
}
