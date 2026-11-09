package com.sportplus.pulsepower.training.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SetPlanPeriodResource(@NotNull LocalDate from, @NotNull LocalDate to) {
}
