package com.example.imagehub.domain.model;

import com.example.imagehub.adapter.out.persistence.UserJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String userId;
    private String username;
    @Setter
    private String password;
    private String role;

    public static UserModel of(UserJpaEntity userJpaEntity) {
        return new UserModel(userJpaEntity.getUserId(), userJpaEntity.getName(), userJpaEntity.getPassword(), userJpaEntity.getRole());
    }
}
