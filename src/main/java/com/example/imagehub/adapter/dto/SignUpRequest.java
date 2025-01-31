package com.example.imagehub.adapter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {
    private String userId;
    private String username;
    private String password;
    private String role = "USER";
}
