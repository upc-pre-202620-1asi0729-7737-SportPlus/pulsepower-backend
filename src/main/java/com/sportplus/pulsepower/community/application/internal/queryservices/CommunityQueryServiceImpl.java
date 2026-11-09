package com.sportplus.pulsepower.community.application.internal.queryservices;

import com.sportplus.pulsepower.community.application.internal.outboundservices.acl.CommunityActivityGateway;
import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.domain.model.aggregates.UserAchievement;
import com.sportplus.pulsepower.community.domain.model.queries.GetEarnedAchievementsByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.queries.GetSharedProgressByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.queries.GetStreakByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.valueobjects.EarnedAchievement;
import com.sportplus.pulsepower.community.domain.model.valueobjects.Streak;
import com.sportplus.pulsepower.community.domain.repositories.AchievementRepository;
import com.sportplus.pulsepower.community.domain.repositories.SharedProgressRepository;
import com.sportplus.pulsepower.community.domain.repositories.UserAchievementRepository;
import com.sportplus.pulsepower.community.domain.services.CommunityQueryService;
import com.sportplus.pulsepower.community.domain.services.StreakCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class CommunityQueryServiceImpl implements CommunityQueryService {

    /** The only rule in the current catalog: seven consecutive days with any recorded activity. */
    private static final String SEVEN_DAY_STREAK_ACHIEVEMENT_CODE = "demo-first-week";
    private static final int SEVEN_DAY_STREAK_THRESHOLD = 7;

    private final SharedProgressRepository sharedProgressRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final CommunityActivityGateway communityActivityGateway;

    public CommunityQueryServiceImpl(SharedProgressRepository sharedProgressRepository,
                                     AchievementRepository achievementRepository,
                                     UserAchievementRepository userAchievementRepository,
                                     CommunityActivityGateway communityActivityGateway) {
        this.sharedProgressRepository = sharedProgressRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.communityActivityGateway = communityActivityGateway;
    }

    @Override
    public List<SharedProgress> handle(GetSharedProgressByUserIdQuery query) {
        return sharedProgressRepository.findAllByUserIdOrderByCreatedAtDesc(query.userId());
    }

    @Override
    public Streak handle(GetStreakByUserIdQuery query) {
        var dates = communityActivityGateway.fetchRecordedDates(query.userId());
        return StreakCalculator.currentStreak(dates, LocalDate.now());
    }

    /**
     * Mirrors the frontend's own lazy evaluation: every time achievements are read, the
     * current streak is checked against the catalog's rules and any newly-earned
     * achievement is recorded before the (now up to date) earned list is returned.
     */
    @Override
    public List<EarnedAchievement> handle(GetEarnedAchievementsByUserIdQuery query) {
        var streak = handle(new GetStreakByUserIdQuery(query.userId()));

        achievementRepository.findByCode(SEVEN_DAY_STREAK_ACHIEVEMENT_CODE).ifPresent(achievement -> {
            var alreadyEarned = userAchievementRepository.existsByUserIdAndAchievementId(query.userId(),
                    achievement.getId());
            if (!alreadyEarned && streak.days() >= SEVEN_DAY_STREAK_THRESHOLD) {
                userAchievementRepository.save(new UserAchievement(query.userId(), achievement.getId(), Instant.now()));
            }
        });

        var achievementsById = new HashMap<Long, com.sportplus.pulsepower.community.domain.model.aggregates.Achievement>();
        achievementRepository.findAll().forEach(achievement -> achievementsById.put(achievement.getId(), achievement));

        return userAchievementRepository.findAllByUserId(query.userId()).stream()
                .filter(earned -> achievementsById.containsKey(earned.getAchievementId()))
                .map(earned -> new EarnedAchievement(achievementsById.get(earned.getAchievementId()), earned.getEarnedAt()))
                .toList();
    }
}
