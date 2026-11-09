package com.sportplus.pulsepower.training.domain.model.commands;

import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;

import java.time.LocalDate;
import java.time.LocalTime;

public record AddPlannedActivityCommand(
        Long userId,
        LocalDate date,
        LocalTime startTime,
        String activity,
        int durationMinutes,
        Intensity intensity,
        ActivityKind kind
) {
}
