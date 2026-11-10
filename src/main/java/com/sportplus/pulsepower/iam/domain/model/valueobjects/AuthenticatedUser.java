package com.sportplus.pulsepower.iam.domain.model.valueobjects;

import com.sportplus.pulsepower.iam.domain.model.aggregates.User;

public record AuthenticatedUser(User user, String token) {
}
