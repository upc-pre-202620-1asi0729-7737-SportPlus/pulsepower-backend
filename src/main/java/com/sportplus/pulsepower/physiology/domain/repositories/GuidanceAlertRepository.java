package com.sportplus.pulsepower.physiology.domain.repositories;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;

import java.util.List;
import java.util.Optional;

public interface GuidanceAlertRepository {

    GuidanceAlert save(GuidanceAlert alert);

    Optional<GuidanceAlert> findById(Long id);

    List<GuidanceAlert> findAllByUserIdOrderByIssuedAtDesc(Long userId);
}
