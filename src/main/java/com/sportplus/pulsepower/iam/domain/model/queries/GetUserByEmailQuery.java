package com.sportplus.pulsepower.iam.domain.model.queries;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;

public record GetUserByEmailQuery(EmailAddress email) {
}
