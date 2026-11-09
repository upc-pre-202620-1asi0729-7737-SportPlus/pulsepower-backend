package com.sportplus.pulsepower.physiology.domain.model.aggregates;

import java.time.Instant;

public class GuidanceAlert {

    private final Long id;
    private final Long userId;
    private final String title;
    private final String message;
    private final Instant issuedAt;
    private Instant reviewedAt;

    public GuidanceAlert(Long userId, String title, String message, Instant issuedAt) {
        this(null, userId, title, message, issuedAt, null);
    }

    public GuidanceAlert(Long id, Long userId, String title, String message, Instant issuedAt, Instant reviewedAt) {
        if (userId == null) throw new IllegalArgumentException("An alert must belong to a user");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Alert title is required");
        if (issuedAt == null) throw new IllegalArgumentException("Issued time is required");
        this.id = id;
        this.userId = userId;
        this.title = title.trim();
        this.message = message == null ? "" : message.trim();
        this.issuedAt = issuedAt;
        this.reviewedAt = reviewedAt;
    }

    public void markReviewed(Instant at) {
        this.reviewedAt = at;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public String getTitle() { return title; }

    public String getMessage() { return message; }

    public Instant getIssuedAt() { return issuedAt; }

    public Instant getReviewedAt() { return reviewedAt; }
}
