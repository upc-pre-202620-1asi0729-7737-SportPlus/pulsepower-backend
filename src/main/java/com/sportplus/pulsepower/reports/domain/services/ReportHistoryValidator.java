package com.sportplus.pulsepower.reports.domain.services;

import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportEntry;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;
import com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException;

import java.time.LocalDate;
import java.util.List;

/**
 * Pure domain rules for whether a report can be generated, mirroring the frontend's
 * own {@code validateReport} (report-request.ts): a report needs at least a week of
 * recorded history overall, and at least a week of history in every category it
 * covers, before it is considered complete enough to hand back.
 */
public final class ReportHistoryValidator {

    private static final long MINIMUM_SPAN_DAYS = 6;

    private ReportHistoryValidator() {
    }

    /**
     * @param entries every entry of the requested types recorded on or before {@code to}
     *                 (not bounded by {@code from} - the span check needs the full history).
     * @return the entries that actually fall within [from, to].
     */
    public static List<ReportEntry> validate(LocalDate from, LocalDate to, List<ReportType> types,
                                             List<ReportEntry> entries) {
        if (from == null || to == null) throw new BusinessRuleViolationException("A date range is required");
        if (from.isAfter(to)) {
            throw new BusinessRuleViolationException("The end date must be on or after the start date.");
        }
        if (types == null || types.isEmpty()) {
            throw new BusinessRuleViolationException("Choose at least one category for your report.");
        }

        var overallDates = entries.stream()
                .filter(entry -> types.contains(entry.type()) && !entry.date().isAfter(to))
                .map(ReportEntry::date)
                .sorted()
                .toList();
        if (overallDates.isEmpty() || spanDays(overallDates) < MINIMUM_SPAN_DAYS) {
            if (!overallDates.isEmpty()) {
                var earliestUsable = overallDates.get(0).plusDays(MINIMUM_SPAN_DAYS);
                throw new BusinessRuleViolationException(
                        "Earliest report date: %s. Continue recording data to complete a week.".formatted(earliestUsable));
            }
            throw new BusinessRuleViolationException(
                    "Reports require at least one week of recorded history. Add earlier records or return after your first week.");
        }

        for (var type : types) {
            var dates = entries.stream()
                    .filter(entry -> entry.type() == type && !entry.date().isAfter(to))
                    .map(ReportEntry::date)
                    .sorted()
                    .toList();
            if (dates.isEmpty() || spanDays(dates) < MINIMUM_SPAN_DAYS) {
                throw new BusinessRuleViolationException(
                        "Each selected category needs a week of history. Uncheck categories without enough records.");
            }
        }

        var selected = entries.stream()
                .filter(entry -> types.contains(entry.type()) && !entry.date().isBefore(from) && !entry.date().isAfter(to))
                .toList();
        if (selected.isEmpty()) {
            throw new BusinessRuleViolationException("There are no records in the selected period.");
        }
        return selected;
    }

    private static long spanDays(List<LocalDate> sortedDates) {
        return java.time.temporal.ChronoUnit.DAYS.between(sortedDates.get(0), sortedDates.get(sortedDates.size() - 1));
    }
}
