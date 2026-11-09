package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities.WellnessCheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WellnessCheckInJpaRepository extends JpaRepository<WellnessCheckInEntity, Long> {

    Optional<WellnessCheckInEntity> findByUserIdAndDate(Long userId, LocalDate date);

    List<WellnessCheckInEntity> findAllByUserIdOrderByDateDesc(Long userId);
}
