package com.sportplus.pulsepower.iam.domain.model.commands;

public record UpdatePersonalGoalCommand(Long userId, Long goalId, boolean completed) {
}
