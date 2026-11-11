package com.sportplus.pulsepower.physiology.interfaces.rest.resources;

public record RecoveryAssessmentResource(
        int level,
        String calculatedAt,
        String explanation
) {
}
