package com.sportplus.pulsepower.iam.domain.model.aggregates;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.SubscriptionStatus;

import java.time.Instant;

public class Subscription {

    private final Long id;
    private final Long userId;
    private Long planId;
    private SubscriptionStatus status;
    private boolean cancelAtPeriodEnd;
    private Instant endsAt;

    public Subscription(Long userId, Long planId) {
        this(null, userId, planId, SubscriptionStatus.ACTIVE, false, null);
    }

    public Subscription(Long id, Long userId, Long planId, SubscriptionStatus status, boolean cancelAtPeriodEnd,
                        Instant endsAt) {
        if (userId == null) throw new IllegalArgumentException("A subscription must belong to a user");
        if (planId == null) throw new IllegalArgumentException("A subscription must reference a plan");
        if (status == null) throw new IllegalArgumentException("Subscription status is required");
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
        this.endsAt = endsAt;
    }

    public void changePlan(Long newPlanId) {
        if (newPlanId == null) throw new IllegalArgumentException("A subscription must reference a plan");
        this.planId = newPlanId;
        this.status = SubscriptionStatus.ACTIVE;
        this.cancelAtPeriodEnd = false;
        this.endsAt = null;
    }

    public void cancel(boolean atPeriodEnd, Instant periodEnd) {
        if (atPeriodEnd) {
            this.cancelAtPeriodEnd = true;
            this.endsAt = periodEnd;
        } else {
            this.status = SubscriptionStatus.CANCELLED;
            this.cancelAtPeriodEnd = false;
            this.endsAt = Instant.now();
        }
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public Long getPlanId() { return planId; }

    public SubscriptionStatus getStatus() { return status; }

    public boolean isCancelAtPeriodEnd() { return cancelAtPeriodEnd; }

    public Instant getEndsAt() { return endsAt; }
}
