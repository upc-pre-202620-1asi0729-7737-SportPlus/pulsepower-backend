package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.Instant;

@Entity(name = "UserAchievement")
public class UserAchievementEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long achievementId;

    @Column(nullable = false)
    private Instant earnedAt;

    protected UserAchievementEntity() {
    }

    public UserAchievementEntity(Long userId, Long achievementId, Instant earnedAt) {
        this.userId = userId;
        this.achievementId = achievementId;
        this.earnedAt = earnedAt;
    }

    public Long getUserId() { return userId; }

    public Long getAchievementId() { return achievementId; }

    public Instant getEarnedAt() { return earnedAt; }
}
