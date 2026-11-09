package com.sportplus.pulsepower.community.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record ShareProgressResource(
        @NotBlank String audience
) {
}
