package com.sportplus.pulsepower.training.domain;

import com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityStatus;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.PlannedActivity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.TrainingPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainingPlanTest {

    private static final LocalDate START = LocalDate.of(2026, 11, 9);
    private static final LocalDate END = LocalDate.of(2026, 11, 15);

    private static PlannedActivity persisted(long id, LocalDate date, ActivityStatus status) {
        return new PlannedActivity(id, date, LocalTime.of(7, 0), "Run", 30, Intensity.MEDIUM,
                ActivityKind.WORKOUT, status);
    }

    private static TrainingPlan planWith(PlannedActivity... activities) {
        return new TrainingPlan(1L, 5L, "Base plan", new TrainingPeriod(START, END), List.of(activities));
    }

    @Test
    void newPlanTrimsNameAndHasNoPeriodOrActivities() {
        var plan = new TrainingPlan(5L, "  My plan ");
        assertEquals("My plan", plan.getName());
        assertNull(plan.getPeriod());
        assertEquals(0, plan.getActivities().size());
    }

    @Test
    void requiresOwnerAndName() {
        assertThrows(IllegalArgumentException.class, () -> new TrainingPlan(null, "Plan"));
        assertThrows(IllegalArgumentException.class, () -> new TrainingPlan(1L, " "));
        assertThrows(IllegalArgumentException.class, () -> new TrainingPlan(1L, null));
    }

    @Test
    void cannotAddActivitiesBeforeSettingThePeriod() {
        var plan = new TrainingPlan(5L, "Plan");
        assertThrows(BusinessRuleViolationException.class, () -> plan.addActivity(START, null, "Run", 30,
                Intensity.LOW, ActivityKind.WORKOUT));
    }

    @Test
    void activitiesMustFallInsideThePeriod() {
        var plan = new TrainingPlan(5L, "Plan");
        plan.setPeriod(START, END);
        assertThrows(BusinessRuleViolationException.class, () -> plan.addActivity(END.plusDays(1), null, "Run", 30,
                Intensity.LOW, ActivityKind.WORKOUT));
        plan.addActivity(START, LocalTime.of(6, 30), "Run", 30, Intensity.LOW, ActivityKind.WORKOUT);
        assertEquals(1, plan.getActivities().size());
        assertEquals(ActivityStatus.PLANNED, plan.getActivities().get(0).status());
    }

    @Test
    void periodCannotExcludeActiveActivitiesButMayExcludeCancelledOnes() {
        var plan = planWith(persisted(1, END, ActivityStatus.PLANNED));
        assertThrows(BusinessRuleViolationException.class, () -> plan.setPeriod(START, END.minusDays(2)));
        assertEquals(END, plan.getPeriod().to());

        var withCancelled = planWith(persisted(1, END, ActivityStatus.CANCELLED));
        withCancelled.setPeriod(START, END.minusDays(2));
        assertEquals(END.minusDays(2), withCancelled.getPeriod().to());
    }

    @Test
    void rescheduleMovesAPlannedActivityWithinThePeriod() {
        var plan = planWith(persisted(1, START, ActivityStatus.PLANNED));
        plan.rescheduleActivity(1L, START.plusDays(2), LocalTime.of(18, 0));
        var moved = plan.findPlannedActivity(1L);
        assertEquals(START.plusDays(2), moved.date());
        assertEquals(LocalTime.of(18, 0), moved.startTime());
        assertEquals(ActivityStatus.PLANNED, moved.status());
    }

    @Test
    void rescheduleRejectsOutOfPeriodAndNonPlannedActivities() {
        var plan = planWith(persisted(1, START, ActivityStatus.PLANNED), persisted(2, START, ActivityStatus.COMPLETED));
        assertThrows(BusinessRuleViolationException.class, () -> plan.rescheduleActivity(1L, END.plusDays(1), null));
        assertThrows(BusinessRuleViolationException.class, () -> plan.rescheduleActivity(2L, START, null));
        assertThrows(ResourceNotFoundException.class, () -> plan.rescheduleActivity(99L, START, null));
    }

    @Test
    void cancelMarksTheActivityCancelled() {
        var plan = planWith(persisted(1, START, ActivityStatus.PLANNED));
        plan.cancelActivity(1L);
        assertEquals(ActivityStatus.CANCELLED, plan.findPlannedActivity(1L).status());
        assertThrows(ResourceNotFoundException.class, () -> plan.cancelActivity(42L));
    }

    @Test
    void completeOnlyWorksOnPlannedActivities() {
        var plan = planWith(persisted(1, START, ActivityStatus.PLANNED), persisted(2, START, ActivityStatus.CANCELLED));
        plan.completeActivity(1L);
        assertEquals(ActivityStatus.COMPLETED, plan.findPlannedActivity(1L).status());
        assertThrows(BusinessRuleViolationException.class, () -> plan.completeActivity(1L));
        assertThrows(BusinessRuleViolationException.class, () -> plan.completeActivity(2L));
    }
}
