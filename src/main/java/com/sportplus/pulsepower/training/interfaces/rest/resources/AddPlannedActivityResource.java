package com.sportplus.pulsepower.training.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AddPlannedActivityResource(
        @NotNull LocalDate date,
        LocalTime startTime,
        @NotBlank @Size(max = 80) String activity,
        @Min(1) @Max(1440) int durationMinutes,
        @NotBlank String intensity,
        @NotBlank String kind
) {
}
