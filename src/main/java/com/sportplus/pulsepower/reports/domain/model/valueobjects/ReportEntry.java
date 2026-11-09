package com.sportplus.pulsepower.reports.domain.model.valueobjects;

import java.time.LocalDate;

/**
 * A single day's worth of activity in one category, gathered through the other
 * bounded contexts' anti-corruption facades. Never persisted on its own: it exists
 * only for the duration of building or validating a report.
 */
public record ReportEntry(LocalDate date, ReportType type, String summary) {
}
