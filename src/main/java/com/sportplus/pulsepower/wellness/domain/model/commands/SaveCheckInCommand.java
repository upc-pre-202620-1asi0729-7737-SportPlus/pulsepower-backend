package com.sportplus.pulsepower.wellness.domain.model.commands;

import java.time.LocalDate;

public record SaveCheckInCommand(Long userId, LocalDate date, int mood, int stress, boolean discomfort, String notes) {
}
