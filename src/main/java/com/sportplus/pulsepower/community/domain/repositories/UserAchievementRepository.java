package com.sportplus.pulsepower.community.domain.repositories;

import com.sportplus.pulsepower.community.domain.model.aggregates.UserAchievement;

import java.util.List;

public interface UserAchievementRepository {

    UserAchievement save(UserAchievement userAchievement);

    List<UserAchievement> findAllByUserId(Long userId);

    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
}
