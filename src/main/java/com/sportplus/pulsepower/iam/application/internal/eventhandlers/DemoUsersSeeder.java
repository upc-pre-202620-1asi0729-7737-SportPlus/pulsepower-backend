package com.sportplus.pulsepower.iam.application.internal.eventhandlers;

import com.sportplus.pulsepower.iam.application.outboundservices.hashing.HashingService;
import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Seeds the demo account used by the PulsePower frontend (pulsepower@gmail.com / 123456,
 * see the webapp README) so the backend can be swapped in without changing the demo flow.
 */
@Component
@ConditionalOnProperty(name = "pulsepower.seed.demo-users.enabled", havingValue = "true")
public class DemoUsersSeeder {

    private final UserRepository userRepository;
    private final HashingService hashingService;

    public DemoUsersSeeder(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    @Order(2)
    @EventListener(ApplicationReadyEvent.class)
    public void seedDemoUser() {
        var email = new EmailAddress("pulsepower@gmail.com");
        if (userRepository.existsByEmail(email)) return;
        var user = new User(email, hashingService.encode("123456"), new DisplayName("PulsePower Demo"),
                Focus.ATHLETE);
        user.addGoal("Run a 10K under 50 minutes");
        user.addGoal("Sleep at least 7.5 hours a night");
        userRepository.save(user);
    }
}
