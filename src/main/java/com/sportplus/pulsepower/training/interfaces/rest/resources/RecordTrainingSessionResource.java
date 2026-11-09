package com.sportplus.pulsepower.training.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RecordTrainingSessionResource(
        @NotNull LocalDate date,
        @NotBlank @Size(max = 80) String activity,
        @Min(1) @Max(1440) int durationMinutes,
        @Min(1) @Max(10) int perceivedEffort,
        @Size(max = 2000) String notes,
        Long plannedActivityId
) {
}
