package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import com.sportplus.pulsepower.sleep.domain.model.valueobjects.SleepSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Entity(name = "SleepRecord")
public class SleepRecordEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = false)
    private Instant endedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SleepSource source;

    private Integer sleepScore;

    @Column(nullable = false, length = 2000)
    private String notes;

    protected SleepRecordEntity() {
    }

    public SleepRecordEntity(Long userId, Instant startedAt, Instant endedAt, SleepSource source,
                             Integer sleepScore, String notes) {
        this.userId = userId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.source = source;
        this.sleepScore = sleepScore;
        this.notes = notes;
    }

    public Long getUserId() { return userId; }

    public Instant getStartedAt() { return startedAt; }

    public Instant getEndedAt() { return endedAt; }

    public SleepSource getSource() { return source; }

    public Integer getSleepScore() { return sleepScore; }

    public String getNotes() { return notes; }
}
