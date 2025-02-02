package com.example.imagehub.adapter.out.token;

import com.example.imagehub.application.port.out.TokenProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProviderAdapter implements TokenProviderPort {

    private final JwtEncoder jwtEncoder; // JWT 생성기
    private final JwtDecoder jwtDecoder; // JWT 디코더

    @Override
    public String generateToken(Authentication authentication) {
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // ROLE_ADMIN

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .claim("roles", authorities) // 사용자 역할
                .issuedAt(now) // 발행 시간
                .expiresAt(now.plusSeconds(3600)) // 1시간 유효
                .build();
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    @Override
    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token); // JWT 디코딩
        return jwt.getSubject(); // subject(사용자 id) 반환
    }
}
