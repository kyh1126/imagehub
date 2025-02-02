package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;
    private String password;
    private String name;
    private String role;

    public static UserJpaEntity create(User user) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();

        userJpaEntity.userId = user.getUserId();
        userJpaEntity.password = user.getPassword();
        userJpaEntity.name = user.getUsername();
        userJpaEntity.role = user.getRole();

        return userJpaEntity;
    }
}
