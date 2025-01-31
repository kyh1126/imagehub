package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.UserRepositoryPort;
import com.example.imagehub.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepositoryPort userRepositoryPort;

    /**
     * 사용자 정보를 로드하여 Spring Security 인증에 사용
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserModel userModel = userRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(userModel.getUserId())
                .password(userModel.getPassword())
                .roles(userModel.getRole()) // ROLE_USER, ROLE_ADMIN 등의 역할을 부여
                .build();
    }
}
