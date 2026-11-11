package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.Instant;

@Entity(name = "GuidanceAlert")
public class GuidanceAlertEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private Instant issuedAt;

    private Instant reviewedAt;

    protected GuidanceAlertEntity() {
    }

    public GuidanceAlertEntity(Long userId, String title, String message, Instant issuedAt, Instant reviewedAt) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.issuedAt = issuedAt;
        this.reviewedAt = reviewedAt;
    }

    public Long getUserId() { return userId; }

    public String getTitle() { return title; }

    public String getMessage() { return message; }

    public Instant getIssuedAt() { return issuedAt; }

    public Instant getReviewedAt() { return reviewedAt; }
}
