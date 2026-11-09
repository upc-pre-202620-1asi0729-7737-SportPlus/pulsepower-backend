package com.sportplus.pulsepower.wellness.domain.repositories;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;

import java.util.List;
import java.util.Optional;

public interface WellnessHabitRepository {

    WellnessHabit save(WellnessHabit habit);

    Optional<WellnessHabit> findById(Long id);

    List<WellnessHabit> findAllByUserId(Long userId);

    void deleteById(Long id);
}
