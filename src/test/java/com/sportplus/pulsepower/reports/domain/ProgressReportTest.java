package com.sportplus.pulsepower.reports.domain;

import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProgressReportTest {

    private static final LocalDate FROM = LocalDate.of(2026, 11, 1);
    private static final LocalDate TO = LocalDate.of(2026, 11, 7);
    private static final Instant NOW = Instant.parse("2026-11-07T12:00:00Z");

    private static ProgressReport report(Long userId, LocalDate from, LocalDate to, List<ReportType> types,
                                         String fileName, String document) {
        return new ProgressReport(userId, from, to, types, NOW, fileName, document);
    }

    @Test
    void validReportKeepsItsData() {
        var report = report(1L, FROM, TO, List.of(ReportType.SLEEP), "report.pdf", "QUJD");
        assertEquals(FROM, report.getFrom());
        assertEquals(TO, report.getTo());
        assertEquals("report.pdf", report.getFileName());
        assertEquals(List.of(ReportType.SLEEP), report.getTypes());
    }

    @Test
    void typesAreCopiedDefensively() {
        var types = new ArrayList<>(List.of(ReportType.SLEEP));
        var report = report(1L, FROM, TO, types, "report.pdf", "QUJD");
        types.add(ReportType.TRAINING);
        assertEquals(1, report.getTypes().size());
        assertThrows(UnsupportedOperationException.class, () -> report.getTypes().add(ReportType.WELLNESS));
    }

    @Test
    void requiresOwnerRangeCategoriesFileAndDocument() {
        var types = List.of(ReportType.SLEEP);
        assertThrows(IllegalArgumentException.class, () -> report(null, FROM, TO, types, "r.pdf", "QUJD"));
        assertThrows(IllegalArgumentException.class, () -> report(1L, null, TO, types, "r.pdf", "QUJD"));
        assertThrows(IllegalArgumentException.class, () -> report(1L, FROM, null, types, "r.pdf", "QUJD"));
        assertThrows(IllegalArgumentException.class, () -> report(1L, TO, FROM, types, "r.pdf", "QUJD"));
        assertThrows(IllegalArgumentException.class, () -> report(1L, FROM, TO, List.of(), "r.pdf", "QUJD"));
        assertThrows(IllegalArgumentException.class, () -> report(1L, FROM, TO, null, "r.pdf", "QUJD"));
        assertThrows(IllegalArgumentException.class, () -> report(1L, FROM, TO, types, " ", "QUJD"));
        assertThrows(IllegalArgumentException.class, () -> report(1L, FROM, TO, types, "r.pdf", ""));
        assertThrows(IllegalArgumentException.class, () -> report(1L, FROM, TO, types, "r.pdf", null));
    }
}
