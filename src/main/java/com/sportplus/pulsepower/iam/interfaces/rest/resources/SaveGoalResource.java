package com.sportplus.pulsepower.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveGoalResource(@NotBlank @Size(max = 200) String description) {
}
