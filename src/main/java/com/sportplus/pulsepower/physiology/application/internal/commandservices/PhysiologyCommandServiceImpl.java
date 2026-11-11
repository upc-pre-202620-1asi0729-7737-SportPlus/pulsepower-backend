package com.sportplus.pulsepower.physiology.application.internal.commandservices;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;
import com.sportplus.pulsepower.physiology.domain.model.commands.ConnectWearableCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.DisconnectWearableCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.ReviewAlertCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.SyncWearableDataCommand;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;
import com.sportplus.pulsepower.physiology.domain.repositories.GuidanceAlertRepository;
import com.sportplus.pulsepower.physiology.domain.repositories.PhysiologicalRecordRepository;
import com.sportplus.pulsepower.physiology.domain.repositories.WearableConnectionRepository;
import com.sportplus.pulsepower.physiology.domain.services.PhysiologyCommandService;
import com.sportplus.pulsepower.physiology.domain.services.RecoveryCalculator;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class PhysiologyCommandServiceImpl implements PhysiologyCommandService {

    private final WearableConnectionRepository wearableConnectionRepository;
    private final PhysiologicalRecordRepository physiologicalRecordRepository;
    private final GuidanceAlertRepository guidanceAlertRepository;
    private final CurrentUserProvider currentUserProvider;

    public PhysiologyCommandServiceImpl(WearableConnectionRepository wearableConnectionRepository,
                                        PhysiologicalRecordRepository physiologicalRecordRepository,
                                        GuidanceAlertRepository guidanceAlertRepository,
                                        CurrentUserProvider currentUserProvider) {
        this.wearableConnectionRepository = wearableConnectionRepository;
        this.physiologicalRecordRepository = physiologicalRecordRepository;
        this.guidanceAlertRepository = guidanceAlertRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public WearableConnection handle(ConnectWearableCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Wearable connection");
        var connection = new WearableConnection(command.userId(), command.provider());
        return wearableConnectionRepository.save(connection);
    }

    @Override
    public WearableConnection handle(DisconnectWearableCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Wearable connection");
        var connection = wearableConnectionRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Wearable connection", command.userId()));
        connection.disconnect();
        return wearableConnectionRepository.save(connection);
    }

    @Override
    public WearableConnection handle(SyncWearableDataCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Wearable connection");
        var connection = wearableConnectionRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Wearable connection", command.userId()));
        connection.recordSync(Instant.now());
        var saved = wearableConnectionRepository.save(connection);

        generateSimulatedReadings(command.userId());

        var recent = physiologicalRecordRepository.findAllByUserIdOrderByRecordedAtDesc(command.userId());
        RecoveryCalculator.assess(recent).ifPresent(assessment -> {
            if (assessment.level() < 40) {
                guidanceAlertRepository.save(new GuidanceAlert(command.userId(), "Recovery is low",
                        assessment.explanation(), Instant.now()));
            }
        });

        return saved;
    }

    @Override
    public GuidanceAlert handle(ReviewAlertCommand command) {
        var alert = guidanceAlertRepository.findById(command.alertId())
                .orElseThrow(() -> new ResourceNotFoundException("Guidance alert", command.alertId()));
        currentUserProvider.ensureOwnership(alert.getUserId(), "Guidance alert");
        alert.markReviewed(Instant.now());
        return guidanceAlertRepository.save(alert);
    }

    /**
     * Produces a plausible-looking set of readings for a sync, in the absence of a real
     * wearable integration. This mirrors the frontend's own recovery-simulation.ts, which
     * documents the same readings as demonstration data, not a medical measurement.
     */
    private void generateSimulatedReadings(Long userId) {
        var random = ThreadLocalRandom.current();
        var now = Instant.now();
        for (var metric : Arrays.asList(Metric.HRV, Metric.RESTING_HR, Metric.HEART_RATE, Metric.STRAIN)) {
            var value = switch (metric) {
                case HRV -> 40 + random.nextDouble(0, 40);
                case RESTING_HR -> 48 + random.nextDouble(0, 20);
                case HEART_RATE -> 60 + random.nextDouble(0, 30);
                case STRAIN -> random.nextDouble(0, 21);
            };
            var unit = switch (metric) {
                case HRV -> "ms";
                case RESTING_HR, HEART_RATE -> "bpm";
                case STRAIN -> "pts";
            };
            physiologicalRecordRepository.save(
                    new PhysiologicalRecord(userId, metric, Math.round(value * 10) / 10.0, unit, now));
        }
    }
}
