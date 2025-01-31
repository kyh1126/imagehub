package com.example.imagehub.adapter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String userId;
    private String username;
    @NotBlank
    private String password;
    private String role = "USER";
}
