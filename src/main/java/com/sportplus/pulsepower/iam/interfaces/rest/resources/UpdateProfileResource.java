package com.sportplus.pulsepower.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileResource(
        @NotBlank @Size(max = 100) String displayName,
        @NotBlank String focus,
        Integer age,
        Double weightKg,
        Double heightCm,
        @Size(max = 80) String mainSport,
        @Size(max = 2000) String trainingRoutine,
        @Size(max = 2000) String physicalHistory
) {
}
