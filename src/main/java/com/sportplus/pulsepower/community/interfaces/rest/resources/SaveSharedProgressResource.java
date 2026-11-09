package com.sportplus.pulsepower.community.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveSharedProgressResource(
        @NotBlank @Size(max = 100) String title,
        @NotBlank @Size(max = 2000) String content
) {
}
