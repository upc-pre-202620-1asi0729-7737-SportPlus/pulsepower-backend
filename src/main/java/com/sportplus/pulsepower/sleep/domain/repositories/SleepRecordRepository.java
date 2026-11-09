package com.sportplus.pulsepower.sleep.domain.repositories;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;

import java.util.List;
import java.util.Optional;

public interface SleepRecordRepository {

    SleepRecord save(SleepRecord record);

    Optional<SleepRecord> findById(Long id);

    List<SleepRecord> findAllByUserIdOrderByStartedAtDesc(Long userId);

    void deleteById(Long id);
}
