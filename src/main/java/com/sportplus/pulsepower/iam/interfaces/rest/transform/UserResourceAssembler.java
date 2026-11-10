package com.sportplus.pulsepower.iam.interfaces.rest.transform;

import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.AuthenticatedUser;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.PersonalGoalResource;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.UserResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toValue;

public final class UserResourceAssembler {

    private UserResourceAssembler() {
    }

    public static UserResource toResource(User user) {
        var goals = user.getGoals().stream()
                .map(goal -> new PersonalGoalResource(goal.id(), goal.description(), goal.completed()))
                .toList();
        return new UserResource(user.getId(), user.getEmail().address(), user.getDisplayName().value(),
                toValue(user.getFocus()), user.getAge(), user.getWeightKg(), user.getHeightCm(),
                user.getMainSport(), user.getTrainingRoutine(), user.getPhysicalHistory(),
                toValue(user.getStatus()), goals);
    }

    public static AuthenticatedUserResource toResource(AuthenticatedUser authenticatedUser) {
        return new AuthenticatedUserResource(toResource(authenticatedUser.user()), authenticatedUser.token());
    }
}
