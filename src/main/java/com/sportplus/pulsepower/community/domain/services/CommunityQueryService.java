package com.sportplus.pulsepower.community.domain.services;

import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.domain.model.queries.GetEarnedAchievementsByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.queries.GetSharedProgressByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.queries.GetStreakByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.valueobjects.EarnedAchievement;
import com.sportplus.pulsepower.community.domain.model.valueobjects.Streak;

import java.util.List;

public interface CommunityQueryService {

    List<SharedProgress> handle(GetSharedProgressByUserIdQuery query);

    Streak handle(GetStreakByUserIdQuery query);

    List<EarnedAchievement> handle(GetEarnedAchievementsByUserIdQuery query);
}
