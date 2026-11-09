package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AchievementJpaRepository extends JpaRepository<AchievementEntity, Long> {

    Optional<AchievementEntity> findByCode(String code);

    boolean existsByCode(String code);
}
