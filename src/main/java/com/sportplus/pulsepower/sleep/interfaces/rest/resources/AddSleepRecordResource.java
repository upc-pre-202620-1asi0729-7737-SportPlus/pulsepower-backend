package com.sportplus.pulsepower.sleep.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record AddSleepRecordResource(
        @NotNull Instant startedAt,
        @NotNull Instant endedAt,
        @NotBlank String source,
        Integer sleepScore,
        @Size(max = 2000) String notes
) {
}
