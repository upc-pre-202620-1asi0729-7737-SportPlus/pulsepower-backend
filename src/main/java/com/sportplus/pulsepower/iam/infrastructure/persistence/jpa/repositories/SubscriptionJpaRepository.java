package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByUserId(Long userId);
}
