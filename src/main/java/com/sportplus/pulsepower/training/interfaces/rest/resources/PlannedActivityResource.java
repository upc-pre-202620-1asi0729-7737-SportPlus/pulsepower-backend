package com.sportplus.pulsepower.training.interfaces.rest.resources;

public record PlannedActivityResource(
        Long id,
        String date,
        String startTime,
        String activity,
        int durationMinutes,
        String intensity,
        String kind,
        String status
) {
}
