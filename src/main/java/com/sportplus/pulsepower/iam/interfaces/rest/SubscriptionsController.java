package com.sportplus.pulsepower.iam.interfaces.rest;

import com.sportplus.pulsepower.iam.domain.model.commands.CancelSubscriptionCommand;
import com.sportplus.pulsepower.iam.domain.model.commands.SubscribeCommand;
import com.sportplus.pulsepower.iam.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.sportplus.pulsepower.iam.domain.model.queries.GetSubscriptionByUserIdQuery;
import com.sportplus.pulsepower.iam.domain.services.SubscriptionCommandService;
import com.sportplus.pulsepower.iam.domain.services.SubscriptionQueryService;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.SubscribeResource;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.SubscriptionPlanResource;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.SubscriptionResource;
import com.sportplus.pulsepower.iam.interfaces.rest.transform.SubscriptionResourceAssembler;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Subscription plans and the authenticated user's subscription")
public class SubscriptionsController {

    private final SubscriptionQueryService subscriptionQueryService;
    private final SubscriptionCommandService subscriptionCommandService;
    private final CurrentUserProvider currentUserProvider;

    public SubscriptionsController(SubscriptionQueryService subscriptionQueryService,
                                   SubscriptionCommandService subscriptionCommandService,
                                   CurrentUserProvider currentUserProvider) {
        this.subscriptionQueryService = subscriptionQueryService;
        this.subscriptionCommandService = subscriptionCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/plans")
    @Operation(summary = "List the available subscription plans")
    public ResponseEntity<List<SubscriptionPlanResource>> getPlans() {
        var plans = subscriptionQueryService.handle(new GetAllSubscriptionPlansQuery()).stream()
                .map(SubscriptionResourceAssembler::toResource).toList();
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/me")
    @Operation(summary = "Get the authenticated user's subscription")
    public ResponseEntity<SubscriptionResource> getCurrentSubscription() {
        var userId = currentUserProvider.requireCurrentUserId();
        return subscriptionQueryService.handle(new GetSubscriptionByUserIdQuery(userId))
                .map(subscription -> ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/me")
    @Operation(summary = "Subscribe the authenticated user to a plan")
    public ResponseEntity<SubscriptionResource> subscribe(@Valid @RequestBody SubscribeResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var subscription = subscriptionCommandService.handle(new SubscribeCommand(userId, resource.planId()));
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }

    @PostMapping("/me/cancel")
    @Operation(summary = "Cancel the authenticated user's subscription")
    public ResponseEntity<SubscriptionResource> cancel(@RequestParam(defaultValue = "false") boolean atPeriodEnd) {
        var userId = currentUserProvider.requireCurrentUserId();
        var subscription = subscriptionCommandService.handle(new CancelSubscriptionCommand(userId, atPeriodEnd));
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }
}
