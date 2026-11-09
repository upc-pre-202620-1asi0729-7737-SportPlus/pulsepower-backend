package com.sportplus.pulsepower.physiology.domain.model.aggregates;

import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Recommendation;

import java.util.List;

/**
 * A composite read model assembled on every request from the user's wearable
 * connection, recent readings, alerts and a freshly computed {@link RecoveryAssessment}.
 * It is never itself persisted, the same way {@link RecoveryAssessment} is not.
 */
public record PhysiologicalOverview(
        WearableConnection connection,
        List<PhysiologicalRecord> records,
        RecoveryAssessment assessment,
        List<Recommendation> recommendations,
        List<GuidanceAlert> alerts,
        boolean available
) {
}
