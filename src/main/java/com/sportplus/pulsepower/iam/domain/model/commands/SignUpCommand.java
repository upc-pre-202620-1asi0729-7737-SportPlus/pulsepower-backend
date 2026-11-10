package com.sportplus.pulsepower.iam.domain.model.commands;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;

public record SignUpCommand(
        EmailAddress email,
        String password,
        DisplayName displayName,
        Focus focus,
        String initialGoal
) {
}
