package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity(name = "WellnessCheckIn")
public class WellnessCheckInEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int mood;

    @Column(nullable = false)
    private int stress;

    @Column(nullable = false)
    private boolean discomfort;

    @Column(nullable = false, length = 2000)
    private String notes;

    protected WellnessCheckInEntity() {
    }

    public WellnessCheckInEntity(Long userId, LocalDate date, int mood, int stress, boolean discomfort, String notes) {
        this.userId = userId;
        this.date = date;
        this.mood = mood;
        this.stress = stress;
        this.discomfort = discomfort;
        this.notes = notes;
    }

    public Long getUserId() { return userId; }

    public LocalDate getDate() { return date; }

    public int getMood() { return mood; }

    public int getStress() { return stress; }

    public boolean isDiscomfort() { return discomfort; }

    public String getNotes() { return notes; }
}
