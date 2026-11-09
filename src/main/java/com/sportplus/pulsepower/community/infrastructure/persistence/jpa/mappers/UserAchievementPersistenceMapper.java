package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.community.domain.model.aggregates.UserAchievement;
import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities.UserAchievementEntity;

public final class UserAchievementPersistenceMapper {

    private UserAchievementPersistenceMapper() {
    }

    public static UserAchievementEntity toEntity(UserAchievement userAchievement) {
        var entity = new UserAchievementEntity(userAchievement.getUserId(), userAchievement.getAchievementId(),
                userAchievement.getEarnedAt());
        entity.setId(userAchievement.getId());
        return entity;
    }

    public static UserAchievement toDomain(UserAchievementEntity entity) {
        return new UserAchievement(entity.getId(), entity.getUserId(), entity.getAchievementId(), entity.getEarnedAt());
    }
}
