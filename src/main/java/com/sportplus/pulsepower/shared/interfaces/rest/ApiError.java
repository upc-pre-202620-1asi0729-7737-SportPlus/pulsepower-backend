package com.sportplus.pulsepower.shared.interfaces.rest;

import java.time.Instant;

public record ApiError(
        int status,
        String message,
        Instant timestamp
) {
}
