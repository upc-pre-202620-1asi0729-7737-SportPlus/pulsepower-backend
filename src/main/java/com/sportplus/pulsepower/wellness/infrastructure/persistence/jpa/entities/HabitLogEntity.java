package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity(name = "HabitLog")
public class HabitLogEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long habitId;

    @Column(nullable = false)
    private LocalDate date;

    protected HabitLogEntity() {
    }

    public HabitLogEntity(Long userId, Long habitId, LocalDate date) {
        this.userId = userId;
        this.habitId = habitId;
        this.date = date;
    }

    public Long getUserId() { return userId; }

    public Long getHabitId() { return habitId; }

    public LocalDate getDate() { return date; }
}
