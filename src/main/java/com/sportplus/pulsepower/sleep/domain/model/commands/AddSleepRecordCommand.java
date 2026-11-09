package com.sportplus.pulsepower.sleep.domain.model.commands;

import com.sportplus.pulsepower.sleep.domain.model.valueobjects.SleepSource;

import java.time.Instant;

public record AddSleepRecordCommand(
        Long userId,
        Instant startedAt,
        Instant endedAt,
        SleepSource source,
        Integer sleepScore,
        String notes
) {
}
