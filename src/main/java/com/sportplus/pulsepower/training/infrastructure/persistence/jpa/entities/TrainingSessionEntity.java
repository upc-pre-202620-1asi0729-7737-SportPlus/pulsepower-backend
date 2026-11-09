package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity(name = "TrainingSession")
public class TrainingSessionEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 80)
    private String activity;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private int perceivedEffort;

    @Column(nullable = false, length = 2000)
    private String notes;

    private Long plannedActivityId;

    protected TrainingSessionEntity() {
    }

    public TrainingSessionEntity(Long userId, LocalDate date, String activity, int durationMinutes,
                                 int perceivedEffort, String notes, Long plannedActivityId) {
        this.userId = userId;
        this.date = date;
        this.activity = activity;
        this.durationMinutes = durationMinutes;
        this.perceivedEffort = perceivedEffort;
        this.notes = notes;
        this.plannedActivityId = plannedActivityId;
    }

    public Long getUserId() { return userId; }

    public LocalDate getDate() { return date; }

    public String getActivity() { return activity; }

    public int getDurationMinutes() { return durationMinutes; }

    public int getPerceivedEffort() { return perceivedEffort; }

    public String getNotes() { return notes; }

    public Long getPlannedActivityId() { return plannedActivityId; }
}
