package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities.UserAchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementJpaRepository extends JpaRepository<UserAchievementEntity, Long> {

    List<UserAchievementEntity> findAllByUserId(Long userId);

    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
}
