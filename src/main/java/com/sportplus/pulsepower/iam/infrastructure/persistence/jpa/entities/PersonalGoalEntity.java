package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "PersonalGoal")
public class PersonalGoalEntity extends AuditableEntity {

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false)
    private boolean completed;

    protected PersonalGoalEntity() {
    }

    public PersonalGoalEntity(Long id, String description, boolean completed) {
        setId(id);
        this.description = description;
        this.completed = completed;
    }

    public String getDescription() { return description; }

    public boolean isCompleted() { return completed; }
}
