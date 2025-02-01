package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.UserPort;
import com.example.imagehub.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserPort {
    private final SpringDataUserRepository userRepository;

    @Override
    public void create(UserModel userModel) {
        UserJpaEntity userJpaEntity = UserJpaEntity.create(userModel);
        userRepository.save(userJpaEntity);
    }

    @Override
    public Optional<UserModel> findByUserId(String userId) {
        return userRepository.findByUserId(userId).map(UserModel::of);
    }
}
