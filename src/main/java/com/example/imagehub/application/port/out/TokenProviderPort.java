package com.example.imagehub.application.port.out;

/**
 * JWT 토큰을 생성 및 검증하는 Port 인터페이스
 */
public interface TokenProviderPort {
    String generateToken(String userId, String role);
    String extractUsername(String token);
}
