package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.iam.domain.model.aggregates.SubscriptionPlan;
import com.sportplus.pulsepower.iam.domain.repositories.SubscriptionPlanRepository;
import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.mappers.SubscriptionPlanPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubscriptionPlanRepositoryAdapter implements SubscriptionPlanRepository {

    private final SubscriptionPlanJpaRepository jpaRepository;

    public SubscriptionPlanRepositoryAdapter(SubscriptionPlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan plan) {
        return SubscriptionPlanPersistenceMapper.toDomain(
                jpaRepository.save(SubscriptionPlanPersistenceMapper.toEntity(plan)));
    }

    @Override
    public Optional<SubscriptionPlan> findById(Long id) {
        return jpaRepository.findById(id).map(SubscriptionPlanPersistenceMapper::toDomain);
    }

    @Override
    public List<SubscriptionPlan> findAll() {
        return jpaRepository.findAll().stream().map(SubscriptionPlanPersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
