package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities.WearableConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WearableConnectionJpaRepository extends JpaRepository<WearableConnectionEntity, Long> {

    Optional<WearableConnectionEntity> findByUserId(Long userId);
}
