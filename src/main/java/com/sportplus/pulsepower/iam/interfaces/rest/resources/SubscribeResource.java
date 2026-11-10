package com.sportplus.pulsepower.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record SubscribeResource(@NotNull Long planId) {
}
