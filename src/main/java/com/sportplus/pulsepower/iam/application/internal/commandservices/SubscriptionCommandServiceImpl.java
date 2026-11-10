package com.sportplus.pulsepower.iam.application.internal.commandservices;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.domain.model.commands.CancelSubscriptionCommand;
import com.sportplus.pulsepower.iam.domain.model.commands.SubscribeCommand;
import com.sportplus.pulsepower.iam.domain.repositories.SubscriptionPlanRepository;
import com.sportplus.pulsepower.iam.domain.repositories.SubscriptionRepository;
import com.sportplus.pulsepower.iam.domain.services.SubscriptionCommandService;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final CurrentUserProvider currentUserProvider;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository,
                                          SubscriptionPlanRepository subscriptionPlanRepository,
                                          CurrentUserProvider currentUserProvider) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Subscription handle(SubscribeCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Subscription");
        if (!subscriptionPlanRepository.existsById(command.planId())) {
            throw new ResourceNotFoundException("Subscription plan", command.planId());
        }
        var subscription = subscriptionRepository.findByUserId(command.userId())
                .orElseGet(() -> new Subscription(command.userId(), command.planId()));
        if (subscription.getId() != null) {
            subscription.changePlan(command.planId());
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription handle(CancelSubscriptionCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Subscription");
        var subscription = subscriptionRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", command.userId()));
        subscription.cancel(command.atPeriodEnd(), Instant.now().plus(30, ChronoUnit.DAYS));
        return subscriptionRepository.save(subscription);
    }
}
