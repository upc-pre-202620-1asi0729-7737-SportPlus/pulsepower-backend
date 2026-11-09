package com.sportplus.pulsepower.wellness.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ToggleHabitLogResource(@NotNull LocalDate date) {
}
