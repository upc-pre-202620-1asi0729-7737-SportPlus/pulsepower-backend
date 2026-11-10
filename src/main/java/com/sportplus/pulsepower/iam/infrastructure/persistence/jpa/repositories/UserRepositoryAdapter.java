package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.mappers.UserPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return UserPersistenceMapper.toDomain(userJpaRepository.save(UserPersistenceMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        return userJpaRepository.findByEmail(email.address()).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(EmailAddress email) {
        return userJpaRepository.existsByEmail(email.address());
    }
}
