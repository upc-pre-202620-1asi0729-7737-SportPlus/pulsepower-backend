package com.sportplus.pulsepower.community.application.internal.eventhandlers;

import com.sportplus.pulsepower.community.domain.model.aggregates.Achievement;
import com.sportplus.pulsepower.community.domain.repositories.AchievementRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Seeds the achievement catalog itself (reference data, not a per-user demo
 * record) so the "seven consecutive days" achievement the frontend awards exists
 * to be earned.
 */
@Component
@ConditionalOnProperty(name = "pulsepower.seed.demo-achievements.enabled", havingValue = "true")
public class DemoAchievementsSeeder {

    private final AchievementRepository achievementRepository;

    public DemoAchievementsSeeder(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    @Order(1)
    @EventListener(ApplicationReadyEvent.class)
    public void seedCatalog() {
        if (achievementRepository.existsByCode("demo-first-week")) return;
        achievementRepository.save(new Achievement("demo-first-week", "Seven consecutive days",
                "Simulated achievement for seven consecutive days with records."));
    }
}
