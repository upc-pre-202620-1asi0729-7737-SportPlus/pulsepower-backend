package com.sportplus.pulsepower.iam.interfaces.rest.transform;

import com.sportplus.pulsepower.iam.domain.model.commands.UpdateProfileCommand;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.UpdateProfileResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toEnum;

public final class ProfileCommandAssembler {

    private ProfileCommandAssembler() {
    }

    public static UpdateProfileCommand toCommand(Long userId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(userId, new DisplayName(resource.displayName()),
                toEnum(Focus.class, resource.focus()), resource.age(), resource.weightKg(), resource.heightCm(),
                resource.mainSport(), resource.trainingRoutine(), resource.physicalHistory());
    }
}
