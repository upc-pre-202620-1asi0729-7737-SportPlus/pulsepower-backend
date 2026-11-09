package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.repositories.TrainingPlanRepository;
import com.sportplus.pulsepower.training.infrastructure.persistence.jpa.mappers.TrainingPlanPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingPlanRepositoryAdapter implements TrainingPlanRepository {

    private final TrainingPlanJpaRepository jpaRepository;

    public TrainingPlanRepositoryAdapter(TrainingPlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TrainingPlan save(TrainingPlan plan) {
        return TrainingPlanPersistenceMapper.toDomain(
                jpaRepository.save(TrainingPlanPersistenceMapper.toEntity(plan)));
    }

    @Override
    public Optional<TrainingPlan> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).map(TrainingPlanPersistenceMapper::toDomain);
    }
}
