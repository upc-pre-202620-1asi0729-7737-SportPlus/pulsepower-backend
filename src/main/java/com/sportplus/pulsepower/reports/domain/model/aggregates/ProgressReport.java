package com.sportplus.pulsepower.reports.domain.model.aggregates;

import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class ProgressReport {

    private final Long id;
    private final Long userId;
    private final LocalDate from;
    private final LocalDate to;
    private final List<ReportType> types;
    private final Instant requestedAt;
    private final String fileName;
    private final String documentBase64;

    public ProgressReport(Long userId, LocalDate from, LocalDate to, List<ReportType> types, Instant requestedAt,
                          String fileName, String documentBase64) {
        this(null, userId, from, to, types, requestedAt, fileName, documentBase64);
    }

    public ProgressReport(Long id, Long userId, LocalDate from, LocalDate to, List<ReportType> types,
                          Instant requestedAt, String fileName, String documentBase64) {
        if (userId == null) throw new IllegalArgumentException("A report must belong to a user");
        if (from == null || to == null) throw new IllegalArgumentException("A report needs a date range");
        if (from.isAfter(to)) throw new IllegalArgumentException("The end date must be on or after the start date");
        if (types == null || types.isEmpty()) throw new IllegalArgumentException("Choose at least one category");
        if (fileName == null || fileName.isBlank()) throw new IllegalArgumentException("File name is required");
        if (documentBase64 == null || documentBase64.isBlank())
            throw new IllegalArgumentException("The report document is required");
        this.id = id;
        this.userId = userId;
        this.from = from;
        this.to = to;
        this.types = List.copyOf(types);
        this.requestedAt = requestedAt;
        this.fileName = fileName;
        this.documentBase64 = documentBase64;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public LocalDate getFrom() { return from; }

    public LocalDate getTo() { return to; }

    public List<ReportType> getTypes() { return types; }

    public Instant getRequestedAt() { return requestedAt; }

    public String getFileName() { return fileName; }

    public String getDocumentBase64() { return documentBase64; }
}
