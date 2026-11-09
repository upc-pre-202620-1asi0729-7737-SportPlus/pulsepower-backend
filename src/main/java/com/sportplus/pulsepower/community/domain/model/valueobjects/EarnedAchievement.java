package com.sportplus.pulsepower.community.domain.model.valueobjects;

import com.sportplus.pulsepower.community.domain.model.aggregates.Achievement;

import java.time.Instant;

public record EarnedAchievement(Achievement achievement, Instant earnedAt) {
}
