package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.AccountStatus;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;
import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "User")
public class UserEntity extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Focus focus;

    private Integer age;
    private Double weightKg;
    private Double heightCm;

    @Column(nullable = false, length = 80)
    private String mainSport;

    @Column(nullable = false, length = 2000)
    private String trainingRoutine;

    @Column(nullable = false, length = 2000)
    private String physicalHistory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @OrderColumn(name = "position")
    private List<PersonalGoalEntity> goals = new ArrayList<>();

    protected UserEntity() {
    }

    public UserEntity(String email, String passwordHash, String displayName, Focus focus) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.focus = focus;
        this.status = AccountStatus.ACTIVE;
        this.mainSport = "";
        this.trainingRoutine = "";
        this.physicalHistory = "";
    }

    public void setProfile(String displayName, Focus focus, Integer age, Double weightKg, Double heightCm,
                           String mainSport, String trainingRoutine, String physicalHistory) {
        this.displayName = displayName;
        this.focus = focus;
        this.age = age;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.mainSport = mainSport;
        this.trainingRoutine = trainingRoutine;
        this.physicalHistory = physicalHistory;
    }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public void setStatus(AccountStatus status) { this.status = status; }

    public void setGoals(List<PersonalGoalEntity> goals) { this.goals = new ArrayList<>(goals); }

    public String getEmail() { return email; }

    public String getPasswordHash() { return passwordHash; }

    public String getDisplayName() { return displayName; }

    public Focus getFocus() { return focus; }

    public Integer getAge() { return age; }

    public Double getWeightKg() { return weightKg; }

    public Double getHeightCm() { return heightCm; }

    public String getMainSport() { return mainSport; }

    public String getTrainingRoutine() { return trainingRoutine; }

    public String getPhysicalHistory() { return physicalHistory; }

    public AccountStatus getStatus() { return status; }

    public List<PersonalGoalEntity> getGoals() { return goals; }
}
