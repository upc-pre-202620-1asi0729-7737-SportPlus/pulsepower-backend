package com.sportplus.pulsepower.wellness.domain.model.aggregates;

public class WellnessHabit {

    private final Long id;
    private final Long userId;
    private String name;
    private int weeklyTarget;

    public WellnessHabit(Long userId, String name, int weeklyTarget) {
        this(null, userId, name, weeklyTarget);
    }

    public WellnessHabit(Long id, Long userId, String name, int weeklyTarget) {
        if (userId == null) throw new IllegalArgumentException("A habit must belong to a user");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Habit name is required");
        if (weeklyTarget < 1 || weeklyTarget > 7) {
            throw new IllegalArgumentException("Weekly target must be between 1 and 7 days");
        }
        this.id = id;
        this.userId = userId;
        this.name = name.trim();
        this.weeklyTarget = weeklyTarget;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public String getName() { return name; }

    public int getWeeklyTarget() { return weeklyTarget; }
}
