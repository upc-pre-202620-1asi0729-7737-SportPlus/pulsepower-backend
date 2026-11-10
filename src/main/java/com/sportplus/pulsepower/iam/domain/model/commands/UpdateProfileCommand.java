package com.sportplus.pulsepower.iam.domain.model.commands;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;

public record UpdateProfileCommand(
        Long userId,
        DisplayName displayName,
        Focus focus,
        Integer age,
        Double weightKg,
        Double heightCm,
        String mainSport,
        String trainingRoutine,
        String physicalHistory
) {
}
