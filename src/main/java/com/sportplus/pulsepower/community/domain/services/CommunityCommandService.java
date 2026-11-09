package com.sportplus.pulsepower.community.domain.services;

import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.domain.model.commands.DeleteSharedProgressCommand;
import com.sportplus.pulsepower.community.domain.model.commands.RevokeProgressShareCommand;
import com.sportplus.pulsepower.community.domain.model.commands.SaveSharedProgressCommand;
import com.sportplus.pulsepower.community.domain.model.commands.ShareProgressCommand;

public interface CommunityCommandService {

    SharedProgress handle(SaveSharedProgressCommand command);

    void handle(DeleteSharedProgressCommand command);

    SharedProgress handle(ShareProgressCommand command);

    SharedProgress handle(RevokeProgressShareCommand command);
}
