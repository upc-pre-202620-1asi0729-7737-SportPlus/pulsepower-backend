package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalTime;

@Entity(name = "SleepRoutine")
public class SleepRoutineEntity extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private LocalTime bedtime;

    @Column(nullable = false)
    private LocalTime wakeTime;

    @Column(nullable = false)
    private double targetHours;

    @Column(nullable = false)
    private boolean reminderEnabled;

    protected SleepRoutineEntity() {
    }

    public SleepRoutineEntity(Long userId, LocalTime bedtime, LocalTime wakeTime, double targetHours,
                              boolean reminderEnabled) {
        this.userId = userId;
        this.bedtime = bedtime;
        this.wakeTime = wakeTime;
        this.targetHours = targetHours;
        this.reminderEnabled = reminderEnabled;
    }

    public Long getUserId() { return userId; }

    public LocalTime getBedtime() { return bedtime; }

    public LocalTime getWakeTime() { return wakeTime; }

    public double getTargetHours() { return targetHours; }

    public boolean isReminderEnabled() { return reminderEnabled; }
}
