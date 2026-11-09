package com.sportplus.pulsepower.training.domain.model.valueobjects;

import java.time.LocalDate;

public record TrainingPeriod(LocalDate from, LocalDate to) {

    public TrainingPeriod {
        if (from == null || to == null) throw new IllegalArgumentException("Both period dates are required");
        if (from.isAfter(to)) throw new IllegalArgumentException("The end date must be on or after the start date");
    }

    public boolean includes(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
