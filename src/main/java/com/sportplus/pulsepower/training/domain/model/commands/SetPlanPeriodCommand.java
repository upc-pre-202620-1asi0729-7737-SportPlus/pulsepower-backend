package com.sportplus.pulsepower.training.domain.model.commands;

import java.time.LocalDate;

public record SetPlanPeriodCommand(Long userId, LocalDate from, LocalDate to) {
}
