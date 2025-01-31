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

    private String userId;
    private String password;
    private String name;
    private String role;

    public static UserJpaEntity of(UserModel userModel) {
        return new UserJpaEntity(null, userModel.getUserId(), userModel.getPassword(), userModel.getUsername(), userModel.getRole());
    }
}
