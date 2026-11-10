package com.sportplus.pulsepower.iam.domain.model.commands;

public record CancelSubscriptionCommand(Long userId, boolean atPeriodEnd) {
}
