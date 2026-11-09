package com.sportplus.pulsepower.wellness.domain.model.commands;

import java.time.LocalDate;

public record ToggleHabitLogCommand(Long userId, Long habitId, LocalDate date) {
}
