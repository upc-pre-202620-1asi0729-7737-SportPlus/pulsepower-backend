package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.domain.repositories.SubscriptionRepository;
import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.mappers.SubscriptionPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;

    public SubscriptionRepositoryAdapter(SubscriptionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return SubscriptionPersistenceMapper.toDomain(
                jpaRepository.save(SubscriptionPersistenceMapper.toEntity(subscription)));
    }

    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).map(SubscriptionPersistenceMapper::toDomain);
    }
}
