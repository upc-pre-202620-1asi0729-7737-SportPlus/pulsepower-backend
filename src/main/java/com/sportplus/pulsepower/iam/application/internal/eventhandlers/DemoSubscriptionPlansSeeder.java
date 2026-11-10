package com.sportplus.pulsepower.iam.application.internal.eventhandlers;

import com.sportplus.pulsepower.iam.domain.model.aggregates.SubscriptionPlan;
import com.sportplus.pulsepower.iam.domain.repositories.SubscriptionPlanRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "pulsepower.seed.demo-subscription-plans.enabled", havingValue = "true")
public class DemoSubscriptionPlansSeeder {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public DemoSubscriptionPlansSeeder(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Order(1)
    @EventListener(ApplicationReadyEvent.class)
    public void seedDemoPlans() {
        seed("Free", List.of("Training log", "Sleep log", "Wellness check-ins", "1 report per month"));
        seed("Pro", List.of("Everything in Free", "Unlimited reports", "Wearable recovery insights",
                "Community sharing"));
    }

    private void seed(String name, List<String> features) {
        if (!subscriptionPlanRepository.existsByName(name)) {
            subscriptionPlanRepository.save(new SubscriptionPlan(name, features));
        }
    }
}
