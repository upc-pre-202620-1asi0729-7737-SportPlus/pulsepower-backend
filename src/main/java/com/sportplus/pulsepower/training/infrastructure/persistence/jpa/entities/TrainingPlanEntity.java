package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "TrainingPlan")
public class TrainingPlanEntity extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String name;

    private LocalDate periodFrom;

    private LocalDate periodTo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "training_plan_id")
    @OrderColumn(name = "position")
    private List<PlannedActivityEntity> activities = new ArrayList<>();

    protected TrainingPlanEntity() {
    }

    public TrainingPlanEntity(Long userId, String name, LocalDate periodFrom, LocalDate periodTo) {
        this.userId = userId;
        this.name = name;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
    }

    public void setActivities(List<PlannedActivityEntity> activities) { this.activities = new ArrayList<>(activities); }

    public Long getUserId() { return userId; }

    public String getName() { return name; }

    public LocalDate getPeriodFrom() { return periodFrom; }

    public LocalDate getPeriodTo() { return periodTo; }

    public List<PlannedActivityEntity> getActivities() { return activities; }
}
