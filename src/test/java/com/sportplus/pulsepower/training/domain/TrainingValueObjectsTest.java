package com.sportplus.pulsepower.training.domain;

import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityStatus;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.PlannedActivity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.TrainingPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingValueObjectsTest {

    private static final LocalDate DAY = LocalDate.of(2026, 11, 10);

    private static PlannedActivity activity(String name, int minutes) {
        return new PlannedActivity(null, DAY, LocalTime.of(7, 0), name, minutes, Intensity.MEDIUM,
                ActivityKind.WORKOUT, ActivityStatus.PLANNED);
    }

    @Test
    void periodRequiresBothDatesInOrder() {
        assertThrows(IllegalArgumentException.class, () -> new TrainingPeriod(null, DAY));
        assertThrows(IllegalArgumentException.class, () -> new TrainingPeriod(DAY, null));
        assertThrows(IllegalArgumentException.class, () -> new TrainingPeriod(DAY.plusDays(1), DAY));
        assertEquals(DAY, new TrainingPeriod(DAY, DAY).from());
    }

    @Test
    void periodIncludesItsBoundariesOnly() {
        var period = new TrainingPeriod(DAY, DAY.plusDays(6));
        assertTrue(period.includes(DAY));
        assertTrue(period.includes(DAY.plusDays(6)));
        assertFalse(period.includes(DAY.minusDays(1)));
        assertFalse(period.includes(DAY.plusDays(7)));
    }

    @Test
    void plannedActivityTrimsItsName() {
        assertEquals("Intervals", activity("  Intervals ", 45).activity());
    }

    @Test
    void plannedActivityValidatesNameAndDuration() {
        assertThrows(IllegalArgumentException.class, () -> activity(null, 30));
        assertThrows(IllegalArgumentException.class, () -> activity(" ", 30));
        assertThrows(IllegalArgumentException.class, () -> activity("x".repeat(81), 30));
        assertEquals(80, activity("x".repeat(80), 30).activity().length());
        assertThrows(IllegalArgumentException.class, () -> activity("Run", 0));
        assertThrows(IllegalArgumentException.class, () -> activity("Run", 1441));
        assertEquals(1440, activity("Run", 1440).durationMinutes());
    }

    @Test
    void plannedActivityRequiresDateIntensityKindAndStatus() {
        assertThrows(IllegalArgumentException.class, () -> new PlannedActivity(null, null, null, "Run", 30,
                Intensity.LOW, ActivityKind.WORKOUT, ActivityStatus.PLANNED));
        assertThrows(IllegalArgumentException.class, () -> new PlannedActivity(null, DAY, null, "Run", 30,
                null, ActivityKind.WORKOUT, ActivityStatus.PLANNED));
        assertThrows(IllegalArgumentException.class, () -> new PlannedActivity(null, DAY, null, "Run", 30,
                Intensity.LOW, null, ActivityStatus.PLANNED));
        assertThrows(IllegalArgumentException.class, () -> new PlannedActivity(null, DAY, null, "Run", 30,
                Intensity.LOW, ActivityKind.WORKOUT, null));
    }

    @Test
    void withersReturnCopiesWithTheChangedField() {
        var original = activity("Run", 30);
        assertEquals(ActivityStatus.COMPLETED, original.withStatus(ActivityStatus.COMPLETED).status());
        assertEquals(ActivityStatus.PLANNED, original.status());
        assertEquals(9L, original.withId(9L).id());
    }
}
