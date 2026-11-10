package com.sportplus.pulsepower.iam.interfaces.rest.resources;

import java.util.List;

public record SubscriptionPlanResource(Long id, String name, List<String> features) {
}
