package com.sportplus.pulsepower.wellness.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateHabitResource(
        @NotBlank @Size(max = 80) String name,
        @Min(1) @Max(7) int weeklyTarget
) {
}
