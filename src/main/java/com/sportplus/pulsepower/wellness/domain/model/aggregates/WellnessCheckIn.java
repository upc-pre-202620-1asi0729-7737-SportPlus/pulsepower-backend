package com.sportplus.pulsepower.wellness.domain.model.aggregates;

import java.time.LocalDate;

public class WellnessCheckIn {

    private final Long id;
    private final Long userId;
    private final LocalDate date;
    private final int mood;
    private final int stress;
    private final boolean discomfort;
    private final String notes;

    public WellnessCheckIn(Long userId, LocalDate date, int mood, int stress, boolean discomfort, String notes) {
        this(null, userId, date, mood, stress, discomfort, notes);
    }

    public WellnessCheckIn(Long id, Long userId, LocalDate date, int mood, int stress, boolean discomfort,
                           String notes) {
        if (userId == null) throw new IllegalArgumentException("A check-in must belong to a user");
        if (date == null) throw new IllegalArgumentException("Check-in date is required");
        if (mood < 1 || mood > 5) throw new IllegalArgumentException("Overall state must be between 1 and 5");
        if (stress < 1 || stress > 10) throw new IllegalArgumentException("Stress must be between 1 and 10");
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.mood = mood;
        this.stress = stress;
        this.discomfort = discomfort;
        this.notes = notes == null ? "" : notes.trim();
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public LocalDate getDate() { return date; }

    public int getMood() { return mood; }

    public int getStress() { return stress; }

    public boolean isDiscomfort() { return discomfort; }

    public String getNotes() { return notes; }
}
