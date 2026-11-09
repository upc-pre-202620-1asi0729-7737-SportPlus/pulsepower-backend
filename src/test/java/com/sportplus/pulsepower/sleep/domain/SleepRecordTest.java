package com.sportplus.pulsepower.sleep.domain;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.valueobjects.SleepSource;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepRecordTest {

    private static final Instant NIGHT_START = Instant.parse("2026-11-01T23:00:00Z");
    private static final Instant NIGHT_END = Instant.parse("2026-11-02T06:30:00Z");

    private static SleepRecord manual(Instant start, Instant end) {
        return new SleepRecord(1L, start, end, SleepSource.MANUAL, null, "  ok  ");
    }

    @Test
    void validRecordComputesDurationAndTrimsNotes() {
        var record = manual(NIGHT_START, NIGHT_END);
        assertEquals(7.5, record.durationHours(), 0.0001);
        assertEquals("ok", record.getNotes());
    }

    @Test
    void nullNotesBecomeEmpty() {
        assertEquals("", new SleepRecord(1L, NIGHT_START, NIGHT_END, SleepSource.MANUAL, null, null).getNotes());
    }

    @Test
    void requiresOwnerTimesAndSource() {
        assertThrows(IllegalArgumentException.class,
                () -> new SleepRecord(null, NIGHT_START, NIGHT_END, SleepSource.MANUAL, null, ""));
        assertThrows(IllegalArgumentException.class,
                () -> new SleepRecord(1L, null, NIGHT_END, SleepSource.MANUAL, null, ""));
        assertThrows(IllegalArgumentException.class,
                () -> new SleepRecord(1L, NIGHT_START, null, SleepSource.MANUAL, null, ""));
        assertThrows(IllegalArgumentException.class,
                () -> new SleepRecord(1L, NIGHT_START, NIGHT_END, null, null, ""));
    }

    @Test
    void wakeUpMustBeStrictlyAfterBedtime() {
        assertThrows(IllegalArgumentException.class, () -> manual(NIGHT_END, NIGHT_START));
        assertThrows(IllegalArgumentException.class, () -> manual(NIGHT_START, NIGHT_START));
    }

    @Test
    void recordCannotReachTwentyFourHours() {
        assertThrows(IllegalArgumentException.class, () -> manual(NIGHT_START, NIGHT_START.plusSeconds(24 * 3600)));
        var almost = manual(NIGHT_START, NIGHT_START.plusSeconds(24 * 3600 - 60));
        assertTrue(almost.durationHours() < 24);
    }

    @Test
    void manualRecordsCannotCarryADeviceScore() {
        assertThrows(IllegalArgumentException.class,
                () -> new SleepRecord(1L, NIGHT_START, NIGHT_END, SleepSource.MANUAL, 80, ""));
        var device = new SleepRecord(1L, NIGHT_START, NIGHT_END, SleepSource.AIOTI, 80, "");
        assertEquals(80, device.getSleepScore());
    }

    @Test
    void overlapDetectionIsExclusiveAtTheBoundaries() {
        var base = manual(NIGHT_START, NIGHT_END);
        assertTrue(base.overlaps(manual(NIGHT_END.minusSeconds(3600), NIGHT_END.plusSeconds(3600))));
        assertTrue(base.overlaps(manual(NIGHT_START.plusSeconds(600), NIGHT_END.minusSeconds(600))));
        assertFalse(base.overlaps(manual(NIGHT_END, NIGHT_END.plusSeconds(3600))));
        assertFalse(base.overlaps(manual(NIGHT_START.minusSeconds(3600), NIGHT_START)));
    }
}
