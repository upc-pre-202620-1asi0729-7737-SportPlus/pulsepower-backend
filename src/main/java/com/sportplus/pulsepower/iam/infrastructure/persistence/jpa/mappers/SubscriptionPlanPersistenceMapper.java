package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.iam.domain.model.aggregates.SubscriptionPlan;
import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities.SubscriptionPlanEntity;

public final class SubscriptionPlanPersistenceMapper {

    private SubscriptionPlanPersistenceMapper() {
    }

    public static SubscriptionPlanEntity toEntity(SubscriptionPlan plan) {
        var entity = new SubscriptionPlanEntity(plan.getName());
        entity.setId(plan.getId());
        entity.setFeatures(plan.getFeatures());
        return entity;
    }

    public static SubscriptionPlan toDomain(SubscriptionPlanEntity entity) {
        return new SubscriptionPlan(entity.getId(), entity.getName(), entity.getFeatures());
    }
}
