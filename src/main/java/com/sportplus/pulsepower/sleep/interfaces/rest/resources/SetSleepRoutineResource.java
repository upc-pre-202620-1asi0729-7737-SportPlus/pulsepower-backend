package com.sportplus.pulsepower.sleep.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record SetSleepRoutineResource(
        @NotNull LocalTime bedtime,
        @NotNull LocalTime wakeTime,
        @DecimalMin("1") @DecimalMax("24") double targetHours,
        boolean reminderEnabled
) {
}
