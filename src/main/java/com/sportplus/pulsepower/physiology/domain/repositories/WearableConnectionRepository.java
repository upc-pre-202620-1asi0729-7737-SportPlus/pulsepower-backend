package com.sportplus.pulsepower.physiology.domain.repositories;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;

import java.util.Optional;

public interface WearableConnectionRepository {

    WearableConnection save(WearableConnection connection);

    Optional<WearableConnection> findByUserId(Long userId);
}
