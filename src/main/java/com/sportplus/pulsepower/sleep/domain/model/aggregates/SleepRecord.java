package com.sportplus.pulsepower.sleep.domain.model.aggregates;

import com.sportplus.pulsepower.sleep.domain.model.valueobjects.SleepSource;

import java.time.Duration;
import java.time.Instant;

public class SleepRecord {

    private final Long id;
    private final Long userId;
    private final Instant startedAt;
    private final Instant endedAt;
    private final SleepSource source;
    private final Integer sleepScore;
    private final String notes;

    public SleepRecord(Long userId, Instant startedAt, Instant endedAt, SleepSource source, Integer sleepScore,
                       String notes) {
        this(null, userId, startedAt, endedAt, source, sleepScore, notes);
    }

    public SleepRecord(Long id, Long userId, Instant startedAt, Instant endedAt, SleepSource source,
                       Integer sleepScore, String notes) {
        if (userId == null) throw new IllegalArgumentException("A sleep record must belong to a user");
        if (startedAt == null || endedAt == null) throw new IllegalArgumentException("Bedtime and wake-up time are required");
        if (!endedAt.isAfter(startedAt)) throw new IllegalArgumentException("Wake-up time must be after bedtime");
        if (Duration.between(startedAt, endedAt).toHours() >= 24) {
            throw new IllegalArgumentException("A sleep record cannot exceed 24 hours");
        }
        if (source == null) throw new IllegalArgumentException("Sleep source is required");
        if (source == SleepSource.MANUAL && sleepScore != null) {
            throw new IllegalArgumentException("Manual records cannot contain a device sleep score");
        }
        this.id = id;
        this.userId = userId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.source = source;
        this.sleepScore = sleepScore;
        this.notes = notes == null ? "" : notes.trim();
    }

    public boolean overlaps(SleepRecord other) {
        return startedAt.isBefore(other.endedAt) && endedAt.isAfter(other.startedAt);
    }

    public double durationHours() {
        return Duration.between(startedAt, endedAt).toMinutes() / 60.0;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public Instant getStartedAt() { return startedAt; }

    public Instant getEndedAt() { return endedAt; }

    public SleepSource getSource() { return source; }

    public Integer getSleepScore() { return sleepScore; }

    public String getNotes() { return notes; }
}
