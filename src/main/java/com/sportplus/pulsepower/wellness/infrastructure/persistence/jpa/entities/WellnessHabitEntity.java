package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "WellnessHabit")
public class WellnessHabitEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private int weeklyTarget;

    protected WellnessHabitEntity() {
    }

    public WellnessHabitEntity(Long userId, String name, int weeklyTarget) {
        this.userId = userId;
        this.name = name;
        this.weeklyTarget = weeklyTarget;
    }

    public Long getUserId() { return userId; }

    public String getName() { return name; }

    public int getWeeklyTarget() { return weeklyTarget; }
}
