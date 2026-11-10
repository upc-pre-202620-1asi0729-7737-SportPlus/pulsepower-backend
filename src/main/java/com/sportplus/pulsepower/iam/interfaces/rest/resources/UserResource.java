package com.sportplus.pulsepower.iam.interfaces.rest.resources;

import java.util.List;

public record UserResource(
        Long id,
        String email,
        String displayName,
        String focus,
        Integer age,
        Double weightKg,
        Double heightCm,
        String mainSport,
        String trainingRoutine,
        String physicalHistory,
        String status,
        List<PersonalGoalResource> goals
) {
}
