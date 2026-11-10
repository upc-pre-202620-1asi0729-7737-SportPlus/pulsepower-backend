package com.sportplus.pulsepower.iam.domain;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.SubscriptionStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTest {

    @Test
    void newSubscriptionIsActiveWithoutEndDate() {
        var subscription = new Subscription(1L, 2L);
        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
        assertFalse(subscription.isCancelAtPeriodEnd());
        assertNull(subscription.getEndsAt());
    }

    @Test
    void requiresUserPlanAndStatus() {
        assertThrows(IllegalArgumentException.class, () -> new Subscription(null, 2L));
        assertThrows(IllegalArgumentException.class, () -> new Subscription(1L, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Subscription(null, 1L, 2L, null, false, null));
    }

    @Test
    void changePlanReactivatesAndClearsCancellation() {
        var subscription = new Subscription(5L, 1L, 2L, SubscriptionStatus.CANCELLED, true, Instant.now());
        subscription.changePlan(3L);
        assertEquals(3L, subscription.getPlanId());
        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
        assertFalse(subscription.isCancelAtPeriodEnd());
        assertNull(subscription.getEndsAt());
        assertThrows(IllegalArgumentException.class, () -> subscription.changePlan(null));
    }

    @Test
    void cancelAtPeriodEndKeepsItActiveUntilThePeriodEnds() {
        var periodEnd = Instant.parse("2026-12-01T00:00:00Z");
        var subscription = new Subscription(1L, 2L);
        subscription.cancel(true, periodEnd);
        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
        assertTrue(subscription.isCancelAtPeriodEnd());
        assertEquals(periodEnd, subscription.getEndsAt());
    }

    @Test
    void immediateCancellationMarksItCancelledNow() {
        var subscription = new Subscription(1L, 2L);
        subscription.cancel(false, null);
        assertEquals(SubscriptionStatus.CANCELLED, subscription.getStatus());
        assertFalse(subscription.isCancelAtPeriodEnd());
        assertNotNull(subscription.getEndsAt());
    }
}
