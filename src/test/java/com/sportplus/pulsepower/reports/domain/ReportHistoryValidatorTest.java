package com.sportplus.pulsepower.reports.domain;

import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportEntry;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;
import com.sportplus.pulsepower.reports.domain.services.ReportHistoryValidator;
import com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportHistoryValidatorTest {

    private static final LocalDate DAY1 = LocalDate.of(2026, 11, 1);

    private static List<ReportEntry> daily(ReportType type, LocalDate first, int days) {
        var entries = new ArrayList<ReportEntry>();
        for (int i = 0; i < days; i++) {
            entries.add(new ReportEntry(first.plusDays(i), type, type + " day " + i));
        }
        return entries;
    }

    private static List<ReportEntry> merge(List<ReportEntry> a, List<ReportEntry> b) {
        var all = new ArrayList<>(a);
        all.addAll(b);
        return all;
    }

    @Test
    void requiresADateRange() {
        var types = List.of(ReportType.TRAINING);
        assertThrows(BusinessRuleViolationException.class,
                () -> ReportHistoryValidator.validate(null, DAY1, types, List.of()));
        assertThrows(BusinessRuleViolationException.class,
                () -> ReportHistoryValidator.validate(DAY1, null, types, List.of()));
    }

    @Test
    void endDateCannotPrecedeStartDate() {
        var exception = assertThrows(BusinessRuleViolationException.class, () -> ReportHistoryValidator.validate(
                DAY1.plusDays(5), DAY1, List.of(ReportType.TRAINING), daily(ReportType.TRAINING, DAY1, 7)));
        assertEquals("The end date must be on or after the start date.", exception.getMessage());
    }

    @Test
    void requiresAtLeastOneCategory() {
        assertThrows(BusinessRuleViolationException.class, () -> ReportHistoryValidator.validate(
                DAY1, DAY1.plusDays(6), List.of(), daily(ReportType.TRAINING, DAY1, 7)));
        assertThrows(BusinessRuleViolationException.class, () -> ReportHistoryValidator.validate(
                DAY1, DAY1.plusDays(6), null, daily(ReportType.TRAINING, DAY1, 7)));
    }

    @Test
    void noHistoryAtAllIsRejected() {
        var exception = assertThrows(BusinessRuleViolationException.class, () -> ReportHistoryValidator.validate(
                DAY1, DAY1.plusDays(6), List.of(ReportType.SLEEP), List.of()));
        assertTrue(exception.getMessage().startsWith("Reports require at least one week"));
    }

    @Test
    void lessThanAWeekOfHistoryReportsTheEarliestUsableDate() {
        var exception = assertThrows(BusinessRuleViolationException.class, () -> ReportHistoryValidator.validate(
                DAY1, DAY1.plusDays(10), List.of(ReportType.TRAINING), daily(ReportType.TRAINING, DAY1, 3)));
        assertEquals("Earliest report date: %s. Continue recording data to complete a week.".formatted(DAY1.plusDays(6)),
                exception.getMessage());
    }

    @Test
    void everySelectedCategoryNeedsItsOwnWeekOfHistory() {
        var entries = merge(daily(ReportType.TRAINING, DAY1, 7), daily(ReportType.SLEEP, DAY1, 2));
        var exception = assertThrows(BusinessRuleViolationException.class, () -> ReportHistoryValidator.validate(
                DAY1, DAY1.plusDays(6), List.of(ReportType.TRAINING, ReportType.SLEEP), entries));
        assertTrue(exception.getMessage().startsWith("Each selected category needs a week of history"));
    }

    @Test
    void exactlySevenDaysOfHistoryIsEnoughAndAllEntriesAreReturned() {
        var entries = daily(ReportType.TRAINING, DAY1, 7);
        var result = ReportHistoryValidator.validate(DAY1, DAY1.plusDays(6), List.of(ReportType.TRAINING), entries);
        assertEquals(7, result.size());
    }

    @Test
    void onlyEntriesInsideTheRangeAndSelectedTypesAreReturned() {
        var entries = merge(daily(ReportType.TRAINING, DAY1, 10), daily(ReportType.SLEEP, DAY1, 10));
        var result = ReportHistoryValidator.validate(DAY1.plusDays(2), DAY1.plusDays(8),
                List.of(ReportType.TRAINING), entries);
        assertEquals(7, result.size());
        assertTrue(result.stream().allMatch(entry -> entry.type() == ReportType.TRAINING));
        assertTrue(result.stream().noneMatch(entry -> entry.date().isBefore(DAY1.plusDays(2))));
        assertTrue(result.stream().noneMatch(entry -> entry.date().isAfter(DAY1.plusDays(8))));
    }

    @Test
    void emptyPeriodAfterHistoryIsRejected() {
        var entries = daily(ReportType.TRAINING, DAY1, 7);
        var exception = assertThrows(BusinessRuleViolationException.class, () -> ReportHistoryValidator.validate(
                DAY1.plusDays(20), DAY1.plusDays(30), List.of(ReportType.TRAINING), entries));
        assertEquals("There are no records in the selected period.", exception.getMessage());
    }
}
