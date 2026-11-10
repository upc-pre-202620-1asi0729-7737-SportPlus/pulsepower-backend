package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "SubscriptionPlan")
public class SubscriptionPlanEntity extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscription_plan_feature", joinColumns = @JoinColumn(name = "plan_id"))
    @OrderColumn(name = "position")
    @Column(name = "feature", length = 200)
    private List<String> features = new ArrayList<>();

    protected SubscriptionPlanEntity() {
    }

    public SubscriptionPlanEntity(String name) {
        this.name = name;
    }

    public void setFeatures(List<String> features) { this.features = new ArrayList<>(features); }

    public String getName() { return name; }

    public List<String> getFeatures() { return features; }
}
