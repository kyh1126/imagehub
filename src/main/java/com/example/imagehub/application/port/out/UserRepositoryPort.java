package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.model.UserModel;

import java.util.Optional;

/**
 * 사용자 저장소를 위한 Port 인터페이스
 */
public interface UserRepositoryPort {
    void save(UserModel userModel);
    Optional<UserModel> findByUserId(String userId);
}
