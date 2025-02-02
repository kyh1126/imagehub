package com.example.imagehub.domain;

import com.example.imagehub.adapter.out.persistence.UserJpaEntity;
import com.example.imagehub.application.port.in.SignUpCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private String userId;
    private String username;
    private String password;
    private String role;

    public static User of(SignUpCommand signUpCommand, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        return new User(
                signUpCommand.getUserId(), signUpCommand.getUsername(), encodedPassword, signUpCommand.getRole()
        );
    }

    public static User mapToDomainEntity(UserJpaEntity userJpaEntity) {
        return new User(
                userJpaEntity.getUserId(), userJpaEntity.getName(), userJpaEntity.getPassword(), userJpaEntity.getRole()
        );
    }
}
