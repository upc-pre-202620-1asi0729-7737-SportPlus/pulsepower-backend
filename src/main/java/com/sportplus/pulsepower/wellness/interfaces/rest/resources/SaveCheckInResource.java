package com.sportplus.pulsepower.wellness.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SaveCheckInResource(
        @NotNull LocalDate date,
        @Min(1) @Max(5) int mood,
        @Min(1) @Max(10) int stress,
        boolean discomfort,
        @Size(max = 2000) String notes
) {
}
