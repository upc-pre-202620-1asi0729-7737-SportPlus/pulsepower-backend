package com.sportplus.pulsepower.physiology.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConnectWearableResource(
        @NotBlank @Size(max = 50) String provider
) {
}
