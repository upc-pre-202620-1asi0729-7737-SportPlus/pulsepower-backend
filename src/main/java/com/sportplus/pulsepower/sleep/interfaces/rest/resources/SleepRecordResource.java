package com.sportplus.pulsepower.sleep.interfaces.rest.resources;

public record SleepRecordResource(
        Long id,
        String startedAt,
        String endedAt,
        String source,
        Integer sleepScore,
        String notes,
        double durationHours
) {
}
