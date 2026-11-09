package com.sportplus.pulsepower.reports.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.infrastructure.persistence.jpa.entities.ProgressReportEntity;

public final class ProgressReportPersistenceMapper {

    private ProgressReportPersistenceMapper() {
    }

    public static ProgressReportEntity toEntity(ProgressReport report) {
        var entity = new ProgressReportEntity(report.getUserId(), report.getFrom(), report.getTo(),
                report.getTypes(), report.getRequestedAt(), report.getFileName(), report.getDocumentBase64());
        entity.setId(report.getId());
        return entity;
    }

    public static ProgressReport toDomain(ProgressReportEntity entity) {
        return new ProgressReport(entity.getId(), entity.getUserId(), entity.getReportFrom(), entity.getReportTo(),
                entity.getTypes(), entity.getRequestedAt(), entity.getFileName(), entity.getDocumentBase64());
    }
}
