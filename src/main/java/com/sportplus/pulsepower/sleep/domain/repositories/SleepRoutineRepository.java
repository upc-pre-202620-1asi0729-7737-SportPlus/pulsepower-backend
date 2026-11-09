package com.sportplus.pulsepower.sleep.domain.repositories;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;

import java.util.Optional;

public interface SleepRoutineRepository {

    SleepRoutine save(SleepRoutine routine);

    Optional<SleepRoutine> findByUserId(Long userId);
}
