package com.sportplus.pulsepower.training.interfaces.rest.resources;

public record TrainingSessionResource(
        Long id,
        String date,
        String activity,
        int durationMinutes,
        int perceivedEffort,
        String notes,
        Long plannedActivityId
) {
}
