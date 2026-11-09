package com.sportplus.pulsepower.training.domain.repositories;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;

import java.util.Optional;

public interface TrainingPlanRepository {

    TrainingPlan save(TrainingPlan plan);

    Optional<TrainingPlan> findByUserId(Long userId);
}
