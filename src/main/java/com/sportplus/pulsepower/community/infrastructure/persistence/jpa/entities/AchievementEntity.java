package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "Achievement")
public class AchievementEntity extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    protected AchievementEntity() {
    }

    public AchievementEntity(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    public String getDescription() { return description; }
}
