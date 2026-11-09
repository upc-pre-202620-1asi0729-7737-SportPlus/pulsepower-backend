package com.sportplus.pulsepower.community.domain.model.aggregates;

import java.time.Instant;

public class UserAchievement {

    private final Long id;
    private final Long userId;
    private final Long achievementId;
    private final Instant earnedAt;

    public UserAchievement(Long userId, Long achievementId, Instant earnedAt) {
        this(null, userId, achievementId, earnedAt);
    }

    public UserAchievement(Long id, Long userId, Long achievementId, Instant earnedAt) {
        if (userId == null) throw new IllegalArgumentException("An earned achievement must belong to a user");
        if (achievementId == null) throw new IllegalArgumentException("Achievement is required");
        if (earnedAt == null) throw new IllegalArgumentException("Earned time is required");
        this.id = id;
        this.userId = userId;
        this.achievementId = achievementId;
        this.earnedAt = earnedAt;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public Long getAchievementId() { return achievementId; }

    public Instant getEarnedAt() { return earnedAt; }
}
