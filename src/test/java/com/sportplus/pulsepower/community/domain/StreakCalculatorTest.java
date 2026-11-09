package com.sportplus.pulsepower.community.domain;

import com.sportplus.pulsepower.community.domain.services.StreakCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StreakCalculatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 7);

    @Test
    void noRecordsMeansNoStreak() {
        var streak = StreakCalculator.currentStreak(List.of(), TODAY);
        assertEquals(0, streak.days());
        assertNull(streak.lastRecordedDate());
    }

    @Test
    void countsConsecutiveDaysEndingToday() {
        var streak = StreakCalculator.currentStreak(
                List.of(TODAY, TODAY.minusDays(1), TODAY.minusDays(2)), TODAY);
        assertEquals(3, streak.days());
        assertEquals(TODAY, streak.lastRecordedDate());
    }

    @Test
    void streakSurvivesUntilEndOfDayWhenOnlyYesterdayIsRecorded() {
        var streak = StreakCalculator.currentStreak(
                List.of(TODAY.minusDays(1), TODAY.minusDays(2)), TODAY);
        assertEquals(2, streak.days());
        assertEquals(TODAY.minusDays(1), streak.lastRecordedDate());
    }

    @Test
    void aMissingDayBreaksTheStreak() {
        var streak = StreakCalculator.currentStreak(
                List.of(TODAY, TODAY.minusDays(1), TODAY.minusDays(3), TODAY.minusDays(4)), TODAY);
        assertEquals(2, streak.days());
    }

    @Test
    void olderRecordsWithAGapToYesterdayDoNotCount() {
        var streak = StreakCalculator.currentStreak(List.of(TODAY.minusDays(2), TODAY.minusDays(3)), TODAY);
        assertEquals(0, streak.days());
        assertNull(streak.lastRecordedDate());
    }

    @Test
    void duplicateDatesAreCountedOnce() {
        var streak = StreakCalculator.currentStreak(List.of(TODAY, TODAY, TODAY.minusDays(1)), TODAY);
        assertEquals(2, streak.days());
    }

    @Test
    void sevenConsecutiveDaysReachTheAchievementThreshold() {
        var dates = java.util.stream.IntStream.range(0, 7).mapToObj(TODAY::minusDays).toList();
        assertEquals(7, StreakCalculator.currentStreak(dates, TODAY).days());
    }
}
