package com.sportplus.pulsepower.iam.interfaces.rest.transform;

import com.sportplus.pulsepower.iam.domain.model.commands.SignInCommand;
import com.sportplus.pulsepower.iam.domain.model.commands.SignUpCommand;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.SignInResource;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.SignUpResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toEnum;

public final class AuthenticationCommandAssembler {

    private AuthenticationCommandAssembler() {
    }

    public static SignUpCommand toCommand(SignUpResource resource) {
        return new SignUpCommand(new EmailAddress(resource.email()), resource.password(),
                new DisplayName(resource.fullName()), toEnum(Focus.class, resource.focus()), resource.goal());
    }

    public static SignInCommand toCommand(SignInResource resource) {
        return new SignInCommand(new EmailAddress(resource.email()), resource.password());
    }
}
