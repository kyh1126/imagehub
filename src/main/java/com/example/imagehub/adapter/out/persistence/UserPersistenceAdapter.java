package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.CreateUserPort;
import com.example.imagehub.application.port.out.LoadUserPort;
import com.example.imagehub.common.PersistenceAdapter;
import com.example.imagehub.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
@PersistenceAdapter
public class UserPersistenceAdapter implements CreateUserPort, LoadUserPort {

    private final SpringDataUserRepository userRepository;

    @Override
    public void create(User user) {
        UserJpaEntity userJpaEntity = UserJpaEntity.create(user);

        userRepository.save(userJpaEntity);
    }

    @Override
    public User loadUser(String userId) {
        return userRepository.findByUserId(userId)
                .map(User::mapToDomainEntity)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
