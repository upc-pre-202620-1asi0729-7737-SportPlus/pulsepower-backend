package com.sportplus.pulsepower.reports.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.domain.repositories.ReportRepository;
import com.sportplus.pulsepower.reports.infrastructure.persistence.jpa.mappers.ProgressReportPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepositoryAdapter implements ReportRepository {

    private final ProgressReportJpaRepository jpaRepository;

    public ReportRepositoryAdapter(ProgressReportJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ProgressReport save(ProgressReport report) {
        return ProgressReportPersistenceMapper.toDomain(
                jpaRepository.save(ProgressReportPersistenceMapper.toEntity(report)));
    }

    @Override
    public Optional<ProgressReport> findById(Long id) {
        return jpaRepository.findById(id).map(ProgressReportPersistenceMapper::toDomain);
    }

    @Override
    public List<ProgressReport> findAllByUserIdOrderByRequestedAtDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByRequestedAtDesc(userId).stream()
                .map(ProgressReportPersistenceMapper::toDomain).toList();
    }
}
