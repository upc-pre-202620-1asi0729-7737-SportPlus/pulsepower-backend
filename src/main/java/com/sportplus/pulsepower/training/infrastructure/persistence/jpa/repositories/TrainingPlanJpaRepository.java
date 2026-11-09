package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities.TrainingPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingPlanJpaRepository extends JpaRepository<TrainingPlanEntity, Long> {

    Optional<TrainingPlanEntity> findByUserId(Long userId);
}
