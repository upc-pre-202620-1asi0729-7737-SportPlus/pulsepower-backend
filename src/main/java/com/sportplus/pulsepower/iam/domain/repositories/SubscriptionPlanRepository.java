package com.sportplus.pulsepower.iam.domain.repositories;

import com.sportplus.pulsepower.iam.domain.model.aggregates.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository {

    SubscriptionPlan save(SubscriptionPlan plan);

    Optional<SubscriptionPlan> findById(Long id);

    List<SubscriptionPlan> findAll();

    boolean existsByName(String name);

    boolean existsById(Long id);
}
