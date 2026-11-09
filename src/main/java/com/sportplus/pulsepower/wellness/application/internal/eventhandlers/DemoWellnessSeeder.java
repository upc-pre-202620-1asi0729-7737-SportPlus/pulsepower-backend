package com.sportplus.pulsepower.wellness.application.internal.eventhandlers;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.domain.repositories.HabitLogRepository;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessCheckInRepository;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessHabitRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Seeds ten days of wellness check-ins plus a couple of habits with some logged
 * days for the demo user.
 */
@Component
@ConditionalOnProperty(name = "pulsepower.seed.demo-wellness.enabled", havingValue = "true")
public class DemoWellnessSeeder {

    private static final String DEMO_EMAIL = "pulsepower@gmail.com";

    private final UserRepository userRepository;
    private final WellnessCheckInRepository wellnessCheckInRepository;
    private final WellnessHabitRepository wellnessHabitRepository;
    private final HabitLogRepository habitLogRepository;

    public DemoWellnessSeeder(UserRepository userRepository, WellnessCheckInRepository wellnessCheckInRepository,
                              WellnessHabitRepository wellnessHabitRepository, HabitLogRepository habitLogRepository) {
        this.userRepository = userRepository;
        this.wellnessCheckInRepository = wellnessCheckInRepository;
        this.wellnessHabitRepository = wellnessHabitRepository;
        this.habitLogRepository = habitLogRepository;
    }

    @Order(5)
    @EventListener(ApplicationReadyEvent.class)
    public void seedWellness() {
        var user = userRepository.findByEmail(new EmailAddress(DEMO_EMAIL)).orElse(null);
        if (user == null || !wellnessHabitRepository.findAllByUserId(user.getId()).isEmpty()) return;

        var today = LocalDate.now();
        for (var i = 0; i < 10; i++) {
            var mood = 3 + (i % 3);
            var stress = 2 + (i % 5);
            wellnessCheckInRepository.save(
                    new WellnessCheckIn(user.getId(), today.minusDays(i), mood, stress, i % 4 == 0, "Demo check-in"));
        }

        var stretching = wellnessHabitRepository.save(new WellnessHabit(user.getId(), "Stretching", 5));
        var hydration = wellnessHabitRepository.save(new WellnessHabit(user.getId(), "Hydration", 7));
        for (var i = 0; i < 5; i++) {
            habitLogRepository.save(new HabitLog(user.getId(), stretching.getId(), today.minusDays(i)));
        }
        for (var i = 0; i < 7; i++) {
            habitLogRepository.save(new HabitLog(user.getId(), hydration.getId(), today.minusDays(i)));
        }
    }
}
