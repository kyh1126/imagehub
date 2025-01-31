package com.example.imagehub.application.port.out;

import org.springframework.security.core.Authentication;

/**
 * JWT 토큰을 생성 및 검증하는 Port 인터페이스
 */
public interface TokenProviderPort {
    String generateToken(Authentication authentication);

    String extractUsername(String token);
}
