package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities.GuidanceAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuidanceAlertJpaRepository extends JpaRepository<GuidanceAlertEntity, Long> {

    List<GuidanceAlertEntity> findAllByUserIdOrderByIssuedAtDesc(Long userId);
}
