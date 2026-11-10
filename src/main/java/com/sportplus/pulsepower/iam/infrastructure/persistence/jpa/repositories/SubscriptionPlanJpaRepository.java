package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanJpaRepository extends JpaRepository<SubscriptionPlanEntity, Long> {

    boolean existsByName(String name);
}
