package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityStatus;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "PlannedActivity")
public class PlannedActivityEntity extends AuditableEntity {

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime startTime;

    @Column(nullable = false, length = 80)
    private String activity;

    @Column(nullable = false)
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Intensity intensity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivityKind kind;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ActivityStatus status;

    protected PlannedActivityEntity() {
    }

    public PlannedActivityEntity(Long id, LocalDate date, LocalTime startTime, String activity, int durationMinutes,
                                 Intensity intensity, ActivityKind kind, ActivityStatus status) {
        setId(id);
        this.date = date;
        this.startTime = startTime;
        this.activity = activity;
        this.durationMinutes = durationMinutes;
        this.intensity = intensity;
        this.kind = kind;
        this.status = status;
    }

    public LocalDate getDate() { return date; }

    public LocalTime getStartTime() { return startTime; }

    public String getActivity() { return activity; }

    public int getDurationMinutes() { return durationMinutes; }

    public Intensity getIntensity() { return intensity; }

    public ActivityKind getKind() { return kind; }

    public ActivityStatus getStatus() { return status; }
}
