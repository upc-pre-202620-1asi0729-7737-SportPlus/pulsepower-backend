package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.entities.SleepRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SleepRecordJpaRepository extends JpaRepository<SleepRecordEntity, Long> {

    List<SleepRecordEntity> findAllByUserIdOrderByStartedAtDesc(Long userId);
}
