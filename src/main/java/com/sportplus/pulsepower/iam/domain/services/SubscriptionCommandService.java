package com.sportplus.pulsepower.iam.domain.services;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.domain.model.commands.CancelSubscriptionCommand;
import com.sportplus.pulsepower.iam.domain.model.commands.SubscribeCommand;

public interface SubscriptionCommandService {

    Subscription handle(SubscribeCommand command);

    Subscription handle(CancelSubscriptionCommand command);
}
