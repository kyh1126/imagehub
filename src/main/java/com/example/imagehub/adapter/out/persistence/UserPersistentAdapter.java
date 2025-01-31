package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.UserRepositoryPort;
import com.example.imagehub.domain.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistentAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository userRepository;

    public UserPersistentAdapter(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(UserModel userModel) {
        UserJpaEntity userJpaEntity = UserJpaEntity.of(userModel);
        userRepository.save(userJpaEntity);
    }

    @Override
    public Optional<UserModel> findByUserId(String userId) {
        return userRepository.findByUserId(userId).map(UserModel::of);
    }
}
