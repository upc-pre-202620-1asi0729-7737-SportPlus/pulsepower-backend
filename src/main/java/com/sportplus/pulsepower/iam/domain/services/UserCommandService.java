package com.sportplus.pulsepower.iam.domain.services;

import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.commands.*;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.AuthenticatedUser;

public interface UserCommandService {

    User handle(SignUpCommand command);

    AuthenticatedUser handle(SignInCommand command);

    User handle(UpdateProfileCommand command);

    User handle(ChangePasswordCommand command);

    User handle(DisableAccountCommand command);

    User handle(AddPersonalGoalCommand command);

    User handle(UpdatePersonalGoalCommand command);

    User handle(RemovePersonalGoalCommand command);
}
