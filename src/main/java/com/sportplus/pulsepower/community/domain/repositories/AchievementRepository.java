package com.sportplus.pulsepower.community.domain.repositories;

import com.sportplus.pulsepower.community.domain.model.aggregates.Achievement;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository {

    Achievement save(Achievement achievement);

    Optional<Achievement> findByCode(String code);

    List<Achievement> findAll();

    boolean existsByCode(String code);
}
