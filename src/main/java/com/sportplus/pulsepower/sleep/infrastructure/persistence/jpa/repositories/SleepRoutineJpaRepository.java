package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.entities.SleepRoutineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SleepRoutineJpaRepository extends JpaRepository<SleepRoutineEntity, Long> {

    Optional<SleepRoutineEntity> findByUserId(Long userId);
}
