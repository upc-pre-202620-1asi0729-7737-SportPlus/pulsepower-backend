package com.sportplus.pulsepower.wellness.domain.repositories;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository {

    HabitLog save(HabitLog log);

    Optional<HabitLog> findByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitLog> findAllByUserId(Long userId);

    void delete(HabitLog log);

    void deleteAllByHabitId(Long habitId);
}
