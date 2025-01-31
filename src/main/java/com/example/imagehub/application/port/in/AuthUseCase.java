package com.example.imagehub.application.port.in;

import com.example.imagehub.domain.model.UserModel;

public interface AuthUseCase {
    String signUp(UserModel userModel);
    String login(String userId, String password);
}
