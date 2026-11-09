package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities.HabitLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitLogJpaRepository extends JpaRepository<HabitLogEntity, Long> {

    Optional<HabitLogEntity> findByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitLogEntity> findAllByUserId(Long userId);

    void deleteAllByHabitId(Long habitId);
}
