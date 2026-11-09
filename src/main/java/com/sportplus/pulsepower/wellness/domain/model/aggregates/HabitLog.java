package com.sportplus.pulsepower.wellness.domain.model.aggregates;

import java.time.LocalDate;

public class HabitLog {

    private final Long id;
    private final Long userId;
    private final Long habitId;
    private final LocalDate date;

    public HabitLog(Long userId, Long habitId, LocalDate date) {
        this(null, userId, habitId, date);
    }

    public HabitLog(Long id, Long userId, Long habitId, LocalDate date) {
        if (userId == null) throw new IllegalArgumentException("A habit log must belong to a user");
        if (habitId == null) throw new IllegalArgumentException("A habit log must reference a habit");
        if (date == null) throw new IllegalArgumentException("Habit log date is required");
        this.id = id;
        this.userId = userId;
        this.habitId = habitId;
        this.date = date;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public Long getHabitId() { return habitId; }

    public LocalDate getDate() { return date; }
}
