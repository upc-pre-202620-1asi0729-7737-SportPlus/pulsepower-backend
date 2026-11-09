package com.sportplus.pulsepower.training.domain.model.commands;

import java.time.LocalDate;

public record RecordTrainingSessionCommand(
        Long userId,
        LocalDate date,
        String activity,
        int durationMinutes,
        int perceivedEffort,
        String notes,
        Long plannedActivityId
) {
}
