package com.example.imagehub.adapter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
}
