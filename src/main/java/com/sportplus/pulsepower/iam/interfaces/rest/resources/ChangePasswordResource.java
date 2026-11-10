package com.sportplus.pulsepower.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordResource(
        @NotBlank String currentPassword,
        @NotBlank @Size(min = 8, max = 128) String newPassword
) {
}
