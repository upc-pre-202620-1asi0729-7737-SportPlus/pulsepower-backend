package com.sportplus.pulsepower.physiology.domain.model.aggregates;

import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;

import java.time.Instant;

public class PhysiologicalRecord {

    private final Long id;
    private final Long userId;
    private final Metric metric;
    private final double value;
    private final String unit;
    private final Instant recordedAt;

    public PhysiologicalRecord(Long userId, Metric metric, double value, String unit, Instant recordedAt) {
        this(null, userId, metric, value, unit, recordedAt);
    }

    public PhysiologicalRecord(Long id, Long userId, Metric metric, double value, String unit, Instant recordedAt) {
        if (userId == null) throw new IllegalArgumentException("A record must belong to a user");
        if (metric == null) throw new IllegalArgumentException("Metric is required");
        if (recordedAt == null) throw new IllegalArgumentException("Recorded time is required");
        this.id = id;
        this.userId = userId;
        this.metric = metric;
        this.value = value;
        this.unit = unit == null ? "" : unit.trim();
        this.recordedAt = recordedAt;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public Metric getMetric() { return metric; }

    public double getValue() { return value; }

    public String getUnit() { return unit; }

    public Instant getRecordedAt() { return recordedAt; }
}
