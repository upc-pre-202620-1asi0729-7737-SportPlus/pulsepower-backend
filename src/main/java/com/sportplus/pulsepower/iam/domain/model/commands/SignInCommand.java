package com.sportplus.pulsepower.iam.domain.model.commands;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;

public record SignInCommand(EmailAddress email, String password) {
}
