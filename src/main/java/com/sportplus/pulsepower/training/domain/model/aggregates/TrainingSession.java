package com.sportplus.pulsepower.training.domain.model.aggregates;

import java.time.LocalDate;

public class TrainingSession {

    private final Long id;
    private final Long userId;
    private final LocalDate date;
    private final String activity;
    private final int durationMinutes;
    private final int perceivedEffort;
    private final String notes;
    private final Long plannedActivityId;

    public TrainingSession(Long userId, LocalDate date, String activity, int durationMinutes, int perceivedEffort,
                           String notes, Long plannedActivityId) {
        this(null, userId, date, activity, durationMinutes, perceivedEffort, notes, plannedActivityId);
    }

    public TrainingSession(Long id, Long userId, LocalDate date, String activity, int durationMinutes,
                           int perceivedEffort, String notes, Long plannedActivityId) {
        if (userId == null) throw new IllegalArgumentException("A session must belong to a user");
        if (date == null) throw new IllegalArgumentException("Session date is required");
        if (activity == null || activity.isBlank()) throw new IllegalArgumentException("Activity name is required");
        activity = activity.trim();
        if (activity.length() > 80) throw new IllegalArgumentException("Activity name must be at most 80 characters");
        if (durationMinutes < 1 || durationMinutes > 1440) {
            throw new IllegalArgumentException("Duration must be between 1 and 1440 minutes");
        }
        if (perceivedEffort < 1 || perceivedEffort > 10) {
            throw new IllegalArgumentException("Perceived effort must be between 1 and 10");
        }
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.activity = activity;
        this.durationMinutes = durationMinutes;
        this.perceivedEffort = perceivedEffort;
        this.notes = notes == null ? "" : notes.trim();
        this.plannedActivityId = plannedActivityId;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public LocalDate getDate() { return date; }

    public String getActivity() { return activity; }

    public int getDurationMinutes() { return durationMinutes; }

    public int getPerceivedEffort() { return perceivedEffort; }

    public String getNotes() { return notes; }

    public Long getPlannedActivityId() { return plannedActivityId; }
}
