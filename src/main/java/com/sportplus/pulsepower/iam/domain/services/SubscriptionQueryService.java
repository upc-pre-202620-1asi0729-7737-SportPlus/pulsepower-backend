package com.sportplus.pulsepower.iam.domain.services;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.domain.model.aggregates.SubscriptionPlan;
import com.sportplus.pulsepower.iam.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.sportplus.pulsepower.iam.domain.model.queries.GetSubscriptionByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {

    List<SubscriptionPlan> handle(GetAllSubscriptionPlansQuery query);

    Optional<Subscription> handle(GetSubscriptionByUserIdQuery query);
}
