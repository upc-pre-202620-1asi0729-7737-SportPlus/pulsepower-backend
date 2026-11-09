package com.sportplus.pulsepower.reports.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.reports.infrastructure.persistence.jpa.entities.ProgressReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressReportJpaRepository extends JpaRepository<ProgressReportEntity, Long> {

    List<ProgressReportEntity> findAllByUserIdOrderByRequestedAtDesc(Long userId);
}
