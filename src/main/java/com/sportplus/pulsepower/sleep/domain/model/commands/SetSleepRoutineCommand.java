package com.sportplus.pulsepower.sleep.domain.model.commands;

import java.time.LocalTime;

public record SetSleepRoutineCommand(
        Long userId,
        LocalTime bedtime,
        LocalTime wakeTime,
        double targetHours,
        boolean reminderEnabled
) {
}
