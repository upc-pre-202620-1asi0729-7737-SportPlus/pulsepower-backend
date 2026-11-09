package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;
import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities.GuidanceAlertEntity;

public final class GuidanceAlertPersistenceMapper {

    private GuidanceAlertPersistenceMapper() {
    }

    public static GuidanceAlertEntity toEntity(GuidanceAlert alert) {
        var entity = new GuidanceAlertEntity(alert.getUserId(), alert.getTitle(), alert.getMessage(),
                alert.getIssuedAt(), alert.getReviewedAt());
        entity.setId(alert.getId());
        return entity;
    }

    public static GuidanceAlert toDomain(GuidanceAlertEntity entity) {
        return new GuidanceAlert(entity.getId(), entity.getUserId(), entity.getTitle(), entity.getMessage(),
                entity.getIssuedAt(), entity.getReviewedAt());
    }
}
