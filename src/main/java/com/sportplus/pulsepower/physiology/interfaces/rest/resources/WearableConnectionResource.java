package com.sportplus.pulsepower.physiology.interfaces.rest.resources;

public record WearableConnectionResource(
        Long id,
        String provider,
        String status,
        String lastSyncedAt
) {
}
