package com.sportplus.pulsepower.wellness.domain;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WellnessAggregatesTest {

    private static final LocalDate DAY = LocalDate.of(2026, 11, 8);

    @Test
    void checkInAcceptsTheBoundaryValues() {
        var low = new WellnessCheckIn(1L, DAY, 1, 1, false, null);
        var high = new WellnessCheckIn(1L, DAY, 5, 10, true, "  tense ");
        assertEquals(1, low.getMood());
        assertEquals(5, high.getMood());
        assertEquals(10, high.getStress());
        assertTrue(high.isDiscomfort());
        assertFalse(low.isDiscomfort());
        assertEquals("tense", high.getNotes());
        assertEquals("", low.getNotes());
    }

    @Test
    void checkInRejectsOutOfRangeMoodAndStress() {
        assertThrows(IllegalArgumentException.class, () -> new WellnessCheckIn(1L, DAY, 0, 5, false, ""));
        assertThrows(IllegalArgumentException.class, () -> new WellnessCheckIn(1L, DAY, 6, 5, false, ""));
        assertThrows(IllegalArgumentException.class, () -> new WellnessCheckIn(1L, DAY, 3, 0, false, ""));
        assertThrows(IllegalArgumentException.class, () -> new WellnessCheckIn(1L, DAY, 3, 11, false, ""));
    }

    @Test
    void checkInRequiresOwnerAndDate() {
        assertThrows(IllegalArgumentException.class, () -> new WellnessCheckIn(null, DAY, 3, 5, false, ""));
        assertThrows(IllegalArgumentException.class, () -> new WellnessCheckIn(1L, null, 3, 5, false, ""));
    }

    @Test
    void habitTrimsNameAndEnforcesWeeklyTargetRange() {
        assertEquals("Meditate", new WellnessHabit(1L, "  Meditate ", 3).getName());
        assertEquals(1, new WellnessHabit(1L, "Walk", 1).getWeeklyTarget());
        assertEquals(7, new WellnessHabit(1L, "Walk", 7).getWeeklyTarget());
        assertThrows(IllegalArgumentException.class, () -> new WellnessHabit(1L, "Walk", 0));
        assertThrows(IllegalArgumentException.class, () -> new WellnessHabit(1L, "Walk", 8));
    }

    @Test
    void habitRequiresOwnerAndName() {
        assertThrows(IllegalArgumentException.class, () -> new WellnessHabit(null, "Walk", 3));
        assertThrows(IllegalArgumentException.class, () -> new WellnessHabit(1L, " ", 3));
        assertThrows(IllegalArgumentException.class, () -> new WellnessHabit(1L, null, 3));
    }

    @Test
    void habitLogRequiresOwnerHabitAndDate() {
        var log = new HabitLog(1L, 2L, DAY);
        assertEquals(DAY, log.getDate());
        assertThrows(IllegalArgumentException.class, () -> new HabitLog(null, 2L, DAY));
        assertThrows(IllegalArgumentException.class, () -> new HabitLog(1L, null, DAY));
        assertThrows(IllegalArgumentException.class, () -> new HabitLog(1L, 2L, null));
    }
}
