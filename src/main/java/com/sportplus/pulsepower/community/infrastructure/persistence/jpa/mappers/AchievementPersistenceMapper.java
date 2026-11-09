package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.community.domain.model.aggregates.Achievement;
import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities.AchievementEntity;

public final class AchievementPersistenceMapper {

    private AchievementPersistenceMapper() {
    }

    public static AchievementEntity toEntity(Achievement achievement) {
        var entity = new AchievementEntity(achievement.getCode(), achievement.getName(), achievement.getDescription());
        entity.setId(achievement.getId());
        return entity;
    }

    public static Achievement toDomain(AchievementEntity entity) {
        return new Achievement(entity.getId(), entity.getCode(), entity.getName(), entity.getDescription());
    }
}
