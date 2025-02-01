package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.domain.model.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;
    private String password;
    private String name;
    private String role;

    public static UserJpaEntity create(UserModel userModel) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();

        userJpaEntity.userId = userModel.getUserId();
        userJpaEntity.password = userModel.getPassword();
        userJpaEntity.name = userModel.getUsername();
        userJpaEntity.role = userModel.getRole();

        return userJpaEntity;
    }
}
