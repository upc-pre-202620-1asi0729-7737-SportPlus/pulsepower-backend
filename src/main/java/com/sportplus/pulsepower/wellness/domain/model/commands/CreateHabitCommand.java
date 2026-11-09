package com.sportplus.pulsepower.wellness.domain.model.commands;

public record CreateHabitCommand(Long userId, String name, int weeklyTarget) {
}
