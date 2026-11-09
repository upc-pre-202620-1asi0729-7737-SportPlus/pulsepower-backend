package com.sportplus.pulsepower.sleep.application.internal.eventhandlers;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.domain.model.valueobjects.SleepSource;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRecordRepository;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRoutineRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * Seeds ten nights of sleep history plus a sleep routine for the demo user.
 */
@Component
@ConditionalOnProperty(name = "pulsepower.seed.demo-sleep.enabled", havingValue = "true")
public class DemoSleepSeeder {

    private static final String DEMO_EMAIL = "pulsepower@gmail.com";

    private final UserRepository userRepository;
    private final SleepRecordRepository sleepRecordRepository;
    private final SleepRoutineRepository sleepRoutineRepository;

    public DemoSleepSeeder(UserRepository userRepository, SleepRecordRepository sleepRecordRepository,
                           SleepRoutineRepository sleepRoutineRepository) {
        this.userRepository = userRepository;
        this.sleepRecordRepository = sleepRecordRepository;
        this.sleepRoutineRepository = sleepRoutineRepository;
    }

    @Order(4)
    @EventListener(ApplicationReadyEvent.class)
    public void seedSleep() {
        var user = userRepository.findByEmail(new EmailAddress(DEMO_EMAIL)).orElse(null);
        if (user == null || !sleepRecordRepository.findAllByUserIdOrderByStartedAtDesc(user.getId()).isEmpty()) return;

        var today = LocalDate.now();
        for (var i = 0; i < 10; i++) {
            var startedAt = today.minusDays(i + 1).atTime(LocalTime.of(22, 30)).atZone(ZoneOffset.UTC).toInstant();
            var endedAt = today.minusDays(i).atTime(LocalTime.of(6, 30)).atZone(ZoneOffset.UTC).toInstant();
            var score = 70 + (i % 4) * 7;
            sleepRecordRepository.save(new SleepRecord(user.getId(), startedAt, endedAt, SleepSource.AIOTI, score,
                    "Demo sleep record"));
        }

        sleepRoutineRepository.save(new SleepRoutine(user.getId(), LocalTime.of(22, 30), LocalTime.of(6, 30), 8.0,
                true));
    }
}
