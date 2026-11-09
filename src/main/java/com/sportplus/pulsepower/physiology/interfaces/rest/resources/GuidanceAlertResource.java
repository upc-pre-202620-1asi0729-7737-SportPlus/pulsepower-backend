package com.sportplus.pulsepower.physiology.interfaces.rest.resources;

public record GuidanceAlertResource(
        Long id,
        String title,
        String message,
        String issuedAt,
        String reviewedAt
) {
}
