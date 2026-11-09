package com.sportplus.pulsepower.physiology.domain.model.aggregates;

import java.time.Instant;

/**
 * A point-in-time recovery read-out. Not persisted: it is recomputed from the
 * user's recent {@link PhysiologicalRecord}s every time it is requested, the
 * same way FixCore's analytics context recomputes its summaries on read.
 */
public record RecoveryAssessment(int level, Instant calculatedAt, String explanation) {
}
