package com.sportplus.pulsepower.community.domain.services;

import com.sportplus.pulsepower.community.domain.model.valueobjects.Streak;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pure domain logic for how many consecutive days, counting back from today (or
 * yesterday if nothing is recorded yet today), have a recorded entry. Mirrors the
 * frontend's own {@code currentStreak} (streak.ts).
 */
public final class StreakCalculator {

    private StreakCalculator() {
    }

    public static Streak currentStreak(List<LocalDate> recordedDates, LocalDate today) {
        Set<LocalDate> recorded = new HashSet<>(recordedDates);
        var cursor = recorded.contains(today) ? today : today.minusDays(1);
        var latest = cursor;
        var days = 0;
        while (recorded.contains(cursor)) {
            days++;
            cursor = cursor.minusDays(1);
        }
        return new Streak(days, days > 0 ? latest : null);
    }
}
