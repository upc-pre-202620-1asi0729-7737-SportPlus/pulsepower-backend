package com.sportplus.pulsepower.sleep.domain;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepRoutineTest {

    private static final LocalTime BED = LocalTime.of(23, 0);
    private static final LocalTime WAKE = LocalTime.of(7, 0);

    @Test
    void createsARoutineWithTheGivenValues() {
        var routine = new SleepRoutine(1L, BED, WAKE, 8, true);
        assertEquals(1L, routine.getUserId());
        assertEquals(BED, routine.getBedtime());
        assertEquals(WAKE, routine.getWakeTime());
        assertEquals(8, routine.getTargetHours());
        assertTrue(routine.isReminderEnabled());
    }

    @Test
    void requiresOwnerAndTimes() {
        assertThrows(IllegalArgumentException.class, () -> new SleepRoutine(null, BED, WAKE, 8, false));
        assertThrows(IllegalArgumentException.class, () -> new SleepRoutine(1L, null, WAKE, 8, false));
        assertThrows(IllegalArgumentException.class, () -> new SleepRoutine(1L, BED, null, 8, false));
    }

    @Test
    void targetHoursMustBeBetweenOneAndTwentyFour() {
        assertThrows(IllegalArgumentException.class, () -> new SleepRoutine(1L, BED, WAKE, 0.5, false));
        assertThrows(IllegalArgumentException.class, () -> new SleepRoutine(1L, BED, WAKE, 24.5, false));
        assertEquals(1, new SleepRoutine(1L, BED, WAKE, 1, false).getTargetHours());
        assertEquals(24, new SleepRoutine(1L, BED, WAKE, 24, false).getTargetHours());
    }

    @Test
    void updateReplacesValuesButRejectsInvalidOnes() {
        var routine = new SleepRoutine(1L, BED, WAKE, 8, true);
        routine.update(LocalTime.of(22, 30), LocalTime.of(6, 30), 8, false);
        assertEquals(LocalTime.of(22, 30), routine.getBedtime());
        assertFalse(routine.isReminderEnabled());
        assertThrows(IllegalArgumentException.class, () -> routine.update(BED, WAKE, 30, true));
        assertEquals(LocalTime.of(22, 30), routine.getBedtime());
    }
}
