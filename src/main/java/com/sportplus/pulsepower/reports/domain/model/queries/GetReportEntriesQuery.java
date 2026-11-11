package com.sportplus.pulsepower.reports.domain.model.queries;

import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;

import java.time.LocalDate;
import java.util.List;

public record GetReportEntriesQuery(Long userId, LocalDate from, LocalDate to, List<ReportType> types) {
}
