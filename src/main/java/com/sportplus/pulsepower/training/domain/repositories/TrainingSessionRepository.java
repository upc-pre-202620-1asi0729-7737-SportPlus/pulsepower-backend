package com.sportplus.pulsepower.training.domain.repositories;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionRepository {

    TrainingSession save(TrainingSession session);

    Optional<TrainingSession> findById(Long id);

    List<TrainingSession> findAllByUserIdOrderByDateDesc(Long userId);

    void deleteById(Long id);
}
