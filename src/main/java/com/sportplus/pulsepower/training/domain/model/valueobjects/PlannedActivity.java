package com.sportplus.pulsepower.training.domain.model.valueobjects;

import java.time.LocalDate;
import java.time.LocalTime;

public record PlannedActivity(
        Long id,
        LocalDate date,
        LocalTime startTime,
        String activity,
        int durationMinutes,
        Intensity intensity,
        ActivityKind kind,
        ActivityStatus status
) {

    public PlannedActivity {
        if (date == null) throw new IllegalArgumentException("Activity date is required");
        if (activity == null || activity.isBlank()) throw new IllegalArgumentException("Activity name is required");
        activity = activity.trim();
        if (activity.length() > 80) throw new IllegalArgumentException("Activity name must be at most 80 characters");
        if (durationMinutes < 1 || durationMinutes > 1440) {
            throw new IllegalArgumentException("Duration must be between 1 and 1440 minutes");
        }
        if (intensity == null) throw new IllegalArgumentException("Intensity is required");
        if (kind == null) throw new IllegalArgumentException("Activity type is required");
        if (status == null) throw new IllegalArgumentException("Activity status is required");
    }

    public PlannedActivity withStatus(ActivityStatus newStatus) {
        return new PlannedActivity(id, date, startTime, activity, durationMinutes, intensity, kind, newStatus);
    }

    public PlannedActivity withId(Long newId) {
        return new PlannedActivity(newId, date, startTime, activity, durationMinutes, intensity, kind, status);
    }
}
