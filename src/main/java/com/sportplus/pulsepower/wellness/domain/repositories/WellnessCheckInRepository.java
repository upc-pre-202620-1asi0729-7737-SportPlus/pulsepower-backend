package com.sportplus.pulsepower.wellness.domain.repositories;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WellnessCheckInRepository {

    WellnessCheckIn save(WellnessCheckIn checkIn);

    Optional<WellnessCheckIn> findByUserIdAndDate(Long userId, LocalDate date);

    List<WellnessCheckIn> findAllByUserIdOrderByDateDesc(Long userId);
}
