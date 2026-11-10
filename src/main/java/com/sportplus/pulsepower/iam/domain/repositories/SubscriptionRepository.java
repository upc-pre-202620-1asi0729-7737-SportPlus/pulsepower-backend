package com.sportplus.pulsepower.iam.domain.repositories;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;

import java.util.Optional;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findByUserId(Long userId);
}
