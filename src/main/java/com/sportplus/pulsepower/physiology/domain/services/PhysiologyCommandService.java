package com.sportplus.pulsepower.physiology.domain.services;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;
import com.sportplus.pulsepower.physiology.domain.model.commands.ConnectWearableCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.DisconnectWearableCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.ReviewAlertCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.SyncWearableDataCommand;

public interface PhysiologyCommandService {

    WearableConnection handle(ConnectWearableCommand command);

    WearableConnection handle(DisconnectWearableCommand command);

    WearableConnection handle(SyncWearableDataCommand command);

    GuidanceAlert handle(ReviewAlertCommand command);
}
