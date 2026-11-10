package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.SubscriptionStatus;
import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Entity(name = "Subscription")
public class SubscriptionEntity extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private boolean cancelAtPeriodEnd;

    private Instant endsAt;

    protected SubscriptionEntity() {
    }

    public SubscriptionEntity(Long userId, Long planId, SubscriptionStatus status, boolean cancelAtPeriodEnd,
                              Instant endsAt) {
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
        this.endsAt = endsAt;
    }

    public Long getUserId() { return userId; }

    public Long getPlanId() { return planId; }

    public SubscriptionStatus getStatus() { return status; }

    public boolean isCancelAtPeriodEnd() { return cancelAtPeriodEnd; }

    public Instant getEndsAt() { return endsAt; }
}
