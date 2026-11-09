package com.sportplus.pulsepower.reports.domain.repositories;

import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {

    ProgressReport save(ProgressReport report);

    Optional<ProgressReport> findById(Long id);

    List<ProgressReport> findAllByUserIdOrderByRequestedAtDesc(Long userId);
}
