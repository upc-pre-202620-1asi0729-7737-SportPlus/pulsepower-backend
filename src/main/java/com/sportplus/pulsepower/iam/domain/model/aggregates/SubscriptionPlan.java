package com.sportplus.pulsepower.iam.domain.model.aggregates;

import java.util.List;

public class SubscriptionPlan {

    private final Long id;
    private String name;
    private List<String> features;

    public SubscriptionPlan(String name, List<String> features) {
        this(null, name, features);
    }

    public SubscriptionPlan(Long id, String name, List<String> features) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Plan name is required");
        this.id = id;
        this.name = name.trim();
        this.features = List.copyOf(features == null ? List.of() : features);
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public List<String> getFeatures() { return features; }
}
