package com.sportplus.pulsepower.physiology.application.internal.eventhandlers;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;
import com.sportplus.pulsepower.physiology.domain.repositories.GuidanceAlertRepository;
import com.sportplus.pulsepower.physiology.domain.repositories.PhysiologicalRecordRepository;
import com.sportplus.pulsepower.physiology.domain.repositories.WearableConnectionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * Seeds a connected wearable, ten days of simulated readings and one guidance
 * alert for the demo user.
 */
@Component
@ConditionalOnProperty(name = "pulsepower.seed.demo-physiology.enabled", havingValue = "true")
public class DemoPhysiologySeeder {

    private static final String DEMO_EMAIL = "pulsepower@gmail.com";

    private final UserRepository userRepository;
    private final WearableConnectionRepository wearableConnectionRepository;
    private final PhysiologicalRecordRepository physiologicalRecordRepository;
    private final GuidanceAlertRepository guidanceAlertRepository;

    public DemoPhysiologySeeder(UserRepository userRepository,
                                WearableConnectionRepository wearableConnectionRepository,
                                PhysiologicalRecordRepository physiologicalRecordRepository,
                                GuidanceAlertRepository guidanceAlertRepository) {
        this.userRepository = userRepository;
        this.wearableConnectionRepository = wearableConnectionRepository;
        this.physiologicalRecordRepository = physiologicalRecordRepository;
        this.guidanceAlertRepository = guidanceAlertRepository;
    }

    @Order(6)
    @EventListener(ApplicationReadyEvent.class)
    public void seedPhysiology() {
        var user = userRepository.findByEmail(new EmailAddress(DEMO_EMAIL)).orElse(null);
        if (user == null || wearableConnectionRepository.findByUserId(user.getId()).isPresent()) return;

        var connection = new WearableConnection(user.getId(), "Demo Band");
        connection.recordSync(Instant.now());
        wearableConnectionRepository.save(connection);

        var today = LocalDate.now();
        for (var i = 0; i < 10; i++) {
            var recordedAt = today.minusDays(i).atTime(LocalTime.of(7, 0)).atZone(ZoneOffset.UTC).toInstant();
            physiologicalRecordRepository.save(new PhysiologicalRecord(user.getId(), Metric.HRV,
                    45 + (i % 5) * 3, "ms", recordedAt));
            physiologicalRecordRepository.save(new PhysiologicalRecord(user.getId(), Metric.RESTING_HR,
                    52 + (i % 4), "bpm", recordedAt));
            physiologicalRecordRepository.save(new PhysiologicalRecord(user.getId(), Metric.HEART_RATE,
                    65 + (i % 6), "bpm", recordedAt));
            physiologicalRecordRepository.save(new PhysiologicalRecord(user.getId(), Metric.STRAIN,
                    8 + (i % 7), "pts", recordedAt));
        }

        guidanceAlertRepository.save(new GuidanceAlert(user.getId(), "Recovery is low",
                "Your latest HRV reading is below your recent baseline.", Instant.now()));
    }
}
