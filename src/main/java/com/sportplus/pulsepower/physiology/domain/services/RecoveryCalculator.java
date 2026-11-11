package com.sportplus.pulsepower.physiology.domain.services;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.RecoveryAssessment;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Recommendation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Turns raw wearable readings into a recovery level and a short list of recommendations.
 * This is a pure domain service, with no persistence and no Spring dependencies, exactly
 * like FixCore's {@code MaintenanceMetrics}. The weights below are a placeholder model,
 * the same way the frontend's own recovery-simulation.ts flags its weights as provisional.
 */
public final class RecoveryCalculator {

    private RecoveryCalculator() {
    }

    public static Optional<RecoveryAssessment> assess(List<PhysiologicalRecord> recentRecords) {
        var hrvReadings = recentRecords.stream().filter(r -> r.getMetric() == Metric.HRV).toList();
        if (hrvReadings.isEmpty()) return Optional.empty();

        var latest = hrvReadings.get(0).getValue();
        var baseline = hrvReadings.stream().mapToDouble(PhysiologicalRecord::getValue).average().orElse(latest);
        var deviation = baseline == 0 ? 0 : (latest - baseline) / baseline * 100;
        var level = (int) Math.max(0, Math.min(100, Math.round(60 + deviation)));

        var explanation = level >= 70
                ? "Your latest HRV reading is at or above your recent baseline."
                : level >= 40
                ? "Your latest HRV reading is close to your recent baseline."
                : "Your latest HRV reading is below your recent baseline.";

        return Optional.of(new RecoveryAssessment(level, Instant.now(), explanation));
    }

    public static List<Recommendation> recommend(RecoveryAssessment assessment) {
        if (assessment == null) return List.of();
        if (assessment.level() < 40) {
            return List.of(new Recommendation("prioritize-rest", "Prioritize rest today",
                    "Your recovery level is low compared with your recent baseline.",
                    "Favor an easy or active-rest session and an earlier bedtime tonight."));
        }
        if (assessment.level() < 70) {
            return List.of(new Recommendation("moderate-load", "Keep today's load moderate",
                    "Your recovery is around your recent baseline.",
                    "A regular training session is fine; avoid adding extra intensity."));
        }
        return List.of(new Recommendation("ready-to-push", "You're ready to push",
                "Your recovery is above your recent baseline.",
                "This is a good day for a harder session if your plan calls for one."));
    }
}
