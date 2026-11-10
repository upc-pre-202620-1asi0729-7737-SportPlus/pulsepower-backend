package com.sportplus.pulsepower.iam.interfaces.rest.resources;

import java.time.Instant;

public record SubscriptionResource(
        Long id,
        Long planId,
        String status,
        boolean cancelAtPeriodEnd,
        Instant endsAt
) {
}
