package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities.SharedProgressEntity;

public final class SharedProgressPersistenceMapper {

    private SharedProgressPersistenceMapper() {
    }

    public static SharedProgressEntity toEntity(SharedProgress progress) {
        var entity = new SharedProgressEntity(progress.getUserId(), progress.getTitle(), progress.getContent(),
                progress.getCreatedAt(), progress.getAudiences());
        entity.setId(progress.getId());
        return entity;
    }

    public static SharedProgress toDomain(SharedProgressEntity entity) {
        return new SharedProgress(entity.getId(), entity.getUserId(), entity.getTitle(), entity.getContent(),
                entity.getRecordedAt(), entity.getAudiences());
    }
}
