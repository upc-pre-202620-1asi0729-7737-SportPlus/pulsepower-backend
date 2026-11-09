package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities.TrainingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingSessionJpaRepository extends JpaRepository<TrainingSessionEntity, Long> {

    List<TrainingSessionEntity> findAllByUserIdOrderByDateDesc(Long userId);
}
