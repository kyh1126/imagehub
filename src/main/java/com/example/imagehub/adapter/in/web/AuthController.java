package com.example.imagehub.adapter.in.web;

import com.example.imagehub.adapter.dto.SignInRequest;
import com.example.imagehub.adapter.dto.SignUpRequest;
import com.example.imagehub.application.port.in.AuthUseCase;
import com.example.imagehub.domain.model.UserModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Tag(name = "Auth", description = "회원가입, 로그인 API")
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("signup")
    public Map<String, String> signUp(@RequestBody @Valid SignUpRequest request) {
        authUseCase.signUp(new UserModel(request.getUserId(), request.getUsername(), request.getPassword(), request.getRole()));
        return Map.of("message", "User registered successfully");
    }

    @PostMapping("login")
    public Map<String, String> login(@RequestBody @Valid SignInRequest request) {
        String token = authUseCase.login(request.getUserId(), request.getPassword());
        return Map.of("token", token);
    }
}
