package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities.WellnessHabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WellnessHabitJpaRepository extends JpaRepository<WellnessHabitEntity, Long> {

    List<WellnessHabitEntity> findAllByUserId(Long userId);
}
