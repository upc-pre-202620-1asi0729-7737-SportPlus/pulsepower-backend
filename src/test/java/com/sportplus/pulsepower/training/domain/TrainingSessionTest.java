package com.sportplus.pulsepower.training.domain;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainingSessionTest {

    private static final LocalDate DAY = LocalDate.of(2026, 11, 8);

    private static TrainingSession session(String activity, int minutes, int effort) {
        return new TrainingSession(1L, DAY, activity, minutes, effort, "  felt good ", null);
    }

    @Test
    void validSessionTrimsTextFields() {
        var session = session("  Cycling ", 60, 7);
        assertEquals("Cycling", session.getActivity());
        assertEquals("felt good", session.getNotes());
        assertEquals(60, session.getDurationMinutes());
        assertEquals(7, session.getPerceivedEffort());
        assertNull(session.getPlannedActivityId());
    }

    @Test
    void nullNotesBecomeEmpty() {
        assertEquals("", new TrainingSession(1L, DAY, "Run", 30, 5, null, 3L).getNotes());
    }

    @Test
    void requiresOwnerDateAndActivityName() {
        assertThrows(IllegalArgumentException.class, () -> new TrainingSession(null, DAY, "Run", 30, 5, "", null));
        assertThrows(IllegalArgumentException.class, () -> new TrainingSession(1L, null, "Run", 30, 5, "", null));
        assertThrows(IllegalArgumentException.class, () -> session(null, 30, 5));
        assertThrows(IllegalArgumentException.class, () -> session("  ", 30, 5));
        assertThrows(IllegalArgumentException.class, () -> session("x".repeat(81), 30, 5));
    }

    @Test
    void durationMustBeBetween1And1440Minutes() {
        assertThrows(IllegalArgumentException.class, () -> session("Run", 0, 5));
        assertThrows(IllegalArgumentException.class, () -> session("Run", 1441, 5));
        assertEquals(1, session("Run", 1, 5).getDurationMinutes());
        assertEquals(1440, session("Run", 1440, 5).getDurationMinutes());
    }

    @Test
    void perceivedEffortMustBeBetween1And10() {
        assertThrows(IllegalArgumentException.class, () -> session("Run", 30, 0));
        assertThrows(IllegalArgumentException.class, () -> session("Run", 30, 11));
        assertEquals(1, session("Run", 30, 1).getPerceivedEffort());
        assertEquals(10, session("Run", 30, 10).getPerceivedEffort());
    }
}
