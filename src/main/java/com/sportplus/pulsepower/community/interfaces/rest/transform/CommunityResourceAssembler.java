package com.sportplus.pulsepower.community.interfaces.rest.transform;

import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.domain.model.valueobjects.Audience;
import com.sportplus.pulsepower.community.domain.model.valueobjects.EarnedAchievement;
import com.sportplus.pulsepower.community.domain.model.valueobjects.Streak;
import com.sportplus.pulsepower.community.interfaces.rest.resources.AchievementResource;
import com.sportplus.pulsepower.community.interfaces.rest.resources.SharedProgressResource;
import com.sportplus.pulsepower.community.interfaces.rest.resources.StreakResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toEnum;
import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toValue;

public final class CommunityResourceAssembler {

    private CommunityResourceAssembler() {
    }

    public static SharedProgressResource toResource(SharedProgress progress) {
        var audiences = progress.getAudiences().stream().map(CommunityResourceAssembler::toValuePublic).toList();
        return new SharedProgressResource(progress.getId(), progress.getTitle(), progress.getContent(), "private",
                progress.getCreatedAt().toString(), audiences);
    }

    public static StreakResource toResource(Streak streak) {
        return new StreakResource(streak.days(), streak.lastRecordedDate() == null ? null
                : streak.lastRecordedDate().toString());
    }

    public static AchievementResource toResource(EarnedAchievement earned) {
        return new AchievementResource(earned.achievement().getCode(), earned.achievement().getName(),
                earned.achievement().getDescription(), earned.earnedAt().toString());
    }

    public static Audience toAudience(String value) {
        return toEnum(Audience.class, value);
    }

    private static String toValuePublic(Audience audience) {
        return toValue(audience);
    }
}
