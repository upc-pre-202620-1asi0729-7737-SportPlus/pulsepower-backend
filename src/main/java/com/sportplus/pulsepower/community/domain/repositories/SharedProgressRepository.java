package com.sportplus.pulsepower.community.domain.repositories;

import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;

import java.util.List;
import java.util.Optional;

public interface SharedProgressRepository {

    SharedProgress save(SharedProgress progress);

    Optional<SharedProgress> findById(Long id);

    List<SharedProgress> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    void deleteById(Long id);
}
