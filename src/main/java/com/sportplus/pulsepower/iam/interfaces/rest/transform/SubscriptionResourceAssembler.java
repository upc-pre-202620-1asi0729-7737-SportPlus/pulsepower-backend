package com.sportplus.pulsepower.iam.interfaces.rest.transform;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.domain.model.aggregates.SubscriptionPlan;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.SubscriptionPlanResource;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.SubscriptionResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toValue;

public final class SubscriptionResourceAssembler {

    private SubscriptionResourceAssembler() {
    }

    public static SubscriptionPlanResource toResource(SubscriptionPlan plan) {
        return new SubscriptionPlanResource(plan.getId(), plan.getName(), plan.getFeatures());
    }

    public static SubscriptionResource toResource(Subscription subscription) {
        return new SubscriptionResource(subscription.getId(), subscription.getPlanId(),
                toValue(subscription.getStatus()), subscription.isCancelAtPeriodEnd(), subscription.getEndsAt());
    }
}
