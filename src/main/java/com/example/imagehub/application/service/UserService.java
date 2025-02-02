package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.LoadUserPort;
import com.example.imagehub.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final LoadUserPort loadUserPort;

    /**
     * 사용자 정보를 로드하여 Spring Security 인증에 사용
     *
     * @param userId 사용자 ID (로그인 요청 시 입력되는 ID)
     * @return UserDetails 객체 (Spring Security에서 인증에 사용)
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = loadUserPort.loadUser(userId);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .roles(user.getRole()) // Spring Security가 자동으로 "ROLE_" prefix 추가
                .build();
    }
}
