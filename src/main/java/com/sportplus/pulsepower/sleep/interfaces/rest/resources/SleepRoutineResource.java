package com.sportplus.pulsepower.sleep.interfaces.rest.resources;

public record SleepRoutineResource(
        String bedtime,
        String wakeTime,
        double targetHours,
        boolean reminderEnabled
) {
}
