package com.sportplus.pulsepower.training.domain.model.aggregates;

import com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityStatus;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.PlannedActivity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.TrainingPeriod;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrainingPlan {

    private final Long id;
    private final Long userId;
    private String name;
    private TrainingPeriod period;
    private final List<PlannedActivity> activities;

    public TrainingPlan(Long userId, String name) {
        this(null, userId, name, null, List.of());
    }

    public TrainingPlan(Long id, Long userId, String name, TrainingPeriod period, List<PlannedActivity> activities) {
        if (userId == null) throw new IllegalArgumentException("A training plan must belong to a user");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Plan name is required");
        this.id = id;
        this.userId = userId;
        this.name = name.trim();
        this.period = period;
        this.activities = new ArrayList<>(activities == null ? List.of() : activities);
    }

    public void setPeriod(LocalDate from, LocalDate to) {
        var newPeriod = new TrainingPeriod(from, to);
        var outside = activities.stream()
                .anyMatch(activity -> activity.status() != ActivityStatus.CANCELLED && !newPeriod.includes(activity.date()));
        if (outside) {
            throw new BusinessRuleViolationException("The plan period must include existing activities");
        }
        this.period = newPeriod;
    }

    public void addActivity(LocalDate date, LocalTime startTime, String activity, int durationMinutes,
                            Intensity intensity, ActivityKind kind) {
        if (period == null) {
            throw new BusinessRuleViolationException("Set the plan period before adding activities");
        }
        if (!period.includes(date)) {
            throw new BusinessRuleViolationException("The activity must fall within the plan period");
        }
        activities.add(new PlannedActivity(null, date, startTime, activity, durationMinutes, intensity, kind,
                ActivityStatus.PLANNED));
    }

    public void rescheduleActivity(Long activityId, LocalDate date, LocalTime startTime) {
        var index = indexOfActivity(activityId);
        var old = activities.get(index);
        if (old.status() != ActivityStatus.PLANNED || period == null || !period.includes(date))
            throw new BusinessRuleViolationException("Only planned activities within the plan period can be rescheduled");
        activities.set(index, new PlannedActivity(old.id(), date, startTime, old.activity(),
                old.durationMinutes(), old.intensity(), old.kind(), old.status()));
    }

    public void cancelActivity(Long activityId) {
        var index = indexOfActivity(activityId);
        activities.set(index, activities.get(index).withStatus(ActivityStatus.CANCELLED));
    }

    public void completeActivity(Long activityId) {
        var index = indexOfActivity(activityId);
        var current = activities.get(index);
        if (current.status() != ActivityStatus.PLANNED) {
            throw new BusinessRuleViolationException("This planned activity is no longer available");
        }
        activities.set(index, current.withStatus(ActivityStatus.COMPLETED));
    }

    public PlannedActivity findPlannedActivity(Long activityId) {
        return activities.get(indexOfActivity(activityId));
    }

    private int indexOfActivity(Long activityId) {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).id() != null && activities.get(i).id().equals(activityId)) return i;
        }
        throw new ResourceNotFoundException("Planned activity", activityId);
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public String getName() { return name; }

    public TrainingPeriod getPeriod() { return period; }

    public List<PlannedActivity> getActivities() { return List.copyOf(activities); }
}
