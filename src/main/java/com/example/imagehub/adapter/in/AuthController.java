package com.example.imagehub.adapter.in;

import com.example.imagehub.adapter.dto.SignInRequest;
import com.example.imagehub.adapter.dto.SignUpRequest;
import com.example.imagehub.application.port.in.AuthUseCase;
import com.example.imagehub.application.port.in.SignInCommand;
import com.example.imagehub.application.port.in.SignUpCommand;
import com.example.imagehub.common.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Auth", description = "사용자 인증 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthUseCase authUseCase;

    @Operation(summary = "회원 가입")
    @PostMapping("signup")
    public Map<String, String> signUp(@RequestBody @Valid SignUpRequest request) {
        SignUpCommand signUpCommand =
                new SignUpCommand(request.getUserId(), request.getUsername(), request.getPassword(), request.getRole());

        authUseCase.signUp(signUpCommand);

        return Map.of("message", "User registered successfully");
    }

    @Operation(summary = "로그인")
    @PostMapping("login")
    public Map<String, String> login(@RequestBody @Valid SignInRequest request) {
        SignInCommand signInCommand = new SignInCommand(request.getUserId(), request.getPassword());

        String token = authUseCase.login(signInCommand);

        return Map.of("token", token);
    }
}
