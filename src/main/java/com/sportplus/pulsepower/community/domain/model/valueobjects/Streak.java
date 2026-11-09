package com.sportplus.pulsepower.community.domain.model.valueobjects;

import java.time.LocalDate;

/**
 * Not persisted: recomputed on every request from the recorded dates the other
 * bounded contexts' facades report, the same way the frontend recomputes it from
 * local activity history rather than storing it.
 */
public record Streak(int days, LocalDate lastRecordedDate) {
}
