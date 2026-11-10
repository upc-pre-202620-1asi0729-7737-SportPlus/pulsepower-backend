package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities.SubscriptionEntity;

public final class SubscriptionPersistenceMapper {

    private SubscriptionPersistenceMapper() {
    }

    public static SubscriptionEntity toEntity(Subscription subscription) {
        var entity = new SubscriptionEntity(subscription.getUserId(), subscription.getPlanId(),
                subscription.getStatus(), subscription.isCancelAtPeriodEnd(), subscription.getEndsAt());
        entity.setId(subscription.getId());
        return entity;
    }

    public static Subscription toDomain(SubscriptionEntity entity) {
        return new Subscription(entity.getId(), entity.getUserId(), entity.getPlanId(), entity.getStatus(),
                entity.isCancelAtPeriodEnd(), entity.getEndsAt());
    }
}
