package com.sportplus.pulsepower.reports.domain.model.commands;

import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;

import java.time.LocalDate;
import java.util.List;

public record CreateReportCommand(
        Long userId,
        LocalDate from,
        LocalDate to,
        List<ReportType> types,
        String note,
        String documentBase64
) {
}
