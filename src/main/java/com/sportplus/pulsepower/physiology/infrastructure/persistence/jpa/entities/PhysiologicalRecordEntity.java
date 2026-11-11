package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;
import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Entity(name = "PhysiologicalRecord")
public class PhysiologicalRecordEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Metric metric;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false, length = 10)
    private String unit;

    @Column(nullable = false)
    private Instant recordedAt;

    protected PhysiologicalRecordEntity() {
    }

    public PhysiologicalRecordEntity(Long userId, Metric metric, double value, String unit, Instant recordedAt) {
        this.userId = userId;
        this.metric = metric;
        this.value = value;
        this.unit = unit;
        this.recordedAt = recordedAt;
    }

    public Long getUserId() { return userId; }

    public Metric getMetric() { return metric; }

    public double getValue() { return value; }

    public String getUnit() { return unit; }

    public Instant getRecordedAt() { return recordedAt; }
}
