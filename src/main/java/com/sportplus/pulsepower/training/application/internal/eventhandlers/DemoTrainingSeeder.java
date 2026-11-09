package com.sportplus.pulsepower.training.application.internal.eventhandlers;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;
import com.sportplus.pulsepower.training.domain.repositories.TrainingPlanRepository;
import com.sportplus.pulsepower.training.domain.repositories.TrainingSessionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Seeds ten days of training history plus a couple of upcoming planned activities
 * for the demo user, so training reports and the community streak have something
 * to show right after sign-in.
 */
@Component
@ConditionalOnProperty(name = "pulsepower.seed.demo-training.enabled", havingValue = "true")
public class DemoTrainingSeeder {

    private static final String DEMO_EMAIL = "pulsepower@gmail.com";

    private final UserRepository userRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public DemoTrainingSeeder(UserRepository userRepository, TrainingPlanRepository trainingPlanRepository,
                              TrainingSessionRepository trainingSessionRepository) {
        this.userRepository = userRepository;
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Order(3)
    @EventListener(ApplicationReadyEvent.class)
    public void seedTraining() {
        var user = userRepository.findByEmail(new EmailAddress(DEMO_EMAIL)).orElse(null);
        if (user == null || trainingPlanRepository.findByUserId(user.getId()).isPresent()) return;

        var today = LocalDate.now();
        var plan = new TrainingPlan(user.getId(), "Marathon preparation");
        plan.setPeriod(today.minusDays(14), today.plusDays(14));
        plan.addActivity(today.plusDays(1), LocalTime.of(7, 0), "Interval running", 45, Intensity.HIGH,
                ActivityKind.WORKOUT);
        plan.addActivity(today.plusDays(3), LocalTime.of(7, 0), "Easy jog", 30, Intensity.LOW,
                ActivityKind.ACTIVE_REST);
        trainingPlanRepository.save(plan);

        var activities = new String[]{"Running", "Cycling", "Strength training", "Swimming"};
        for (var i = 0; i < 10; i++) {
            var session = new TrainingSession(user.getId(), today.minusDays(i), activities[i % activities.length],
                    30 + (i % 4) * 10, 4 + (i % 5), "Demo training session", null);
            trainingSessionRepository.save(session);
        }
    }
}
