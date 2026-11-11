package com.sportplus.pulsepower.reports.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;
import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "ProgressReport")
public class ProgressReportEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate reportFrom;

    @Column(nullable = false)
    private LocalDate reportTo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "progress_report_type", joinColumns = @JoinColumn(name = "progress_report_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    @OrderColumn(name = "position")
    private List<ReportType> types;

    @Column(nullable = false)
    private Instant requestedAt;

    @Column(nullable = false, length = 150)
    private String fileName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String documentBase64;

    protected ProgressReportEntity() {
    }

    public ProgressReportEntity(Long userId, LocalDate reportFrom, LocalDate reportTo, List<ReportType> types,
                                Instant requestedAt, String fileName, String documentBase64) {
        this.userId = userId;
        this.reportFrom = reportFrom;
        this.reportTo = reportTo;
        this.types = types;
        this.requestedAt = requestedAt;
        this.fileName = fileName;
        this.documentBase64 = documentBase64;
    }

    public Long getUserId() { return userId; }

    public LocalDate getReportFrom() { return reportFrom; }

    public LocalDate getReportTo() { return reportTo; }

    public List<ReportType> getTypes() { return types; }

    public Instant getRequestedAt() { return requestedAt; }

    public String getFileName() { return fileName; }

    public String getDocumentBase64() { return documentBase64; }
}
