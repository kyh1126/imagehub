package com.example.imagehub.adapter.out.token;

import com.example.imagehub.application.port.out.TokenProviderPort;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenProviderAdapter implements TokenProviderPort {
    private final JwtEncoder jwtEncoder; // JWT 생성기
    private final JwtDecoder jwtDecoder; // JWT 디코더

    public TokenProviderAdapter(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public String generateToken(String userId, String role) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId)
                .claim("roles", role) // 사용자 역할
                .issuedAt(now) // 발행 시간
                .expiresAt(now.plusSeconds(3600)) // 1시간 유효
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token); // JWT 디코딩
        return jwt.getSubject(); // subject(사용자 id) 반환
    }
}
