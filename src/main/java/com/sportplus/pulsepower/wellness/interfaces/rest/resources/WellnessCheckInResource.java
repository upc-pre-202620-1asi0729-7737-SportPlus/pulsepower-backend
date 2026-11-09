package com.sportplus.pulsepower.wellness.interfaces.rest.resources;

public record WellnessCheckInResource(
        Long id,
        String date,
        int mood,
        int stress,
        boolean discomfort,
        String notes
) {
}
