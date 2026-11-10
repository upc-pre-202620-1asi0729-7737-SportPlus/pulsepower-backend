package com.sportplus.pulsepower.iam.domain.model.aggregates;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.AccountStatus;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.PersonalGoal;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final Long id;
    private final EmailAddress email;
    private String passwordHash;
    private DisplayName displayName;
    private Focus focus;
    private Integer age;
    private Double weightKg;
    private Double heightCm;
    private String mainSport;
    private String trainingRoutine;
    private String physicalHistory;
    private AccountStatus status;
    private final List<PersonalGoal> goals;

    public User(EmailAddress email, String passwordHash, DisplayName displayName, Focus focus) {
        this(null, email, passwordHash, displayName, focus, null, null, null, "", "", "",
                AccountStatus.ACTIVE, List.of());
    }

    public User(Long id, EmailAddress email, String passwordHash, DisplayName displayName, Focus focus,
                Integer age, Double weightKg, Double heightCm, String mainSport, String trainingRoutine,
                String physicalHistory, AccountStatus status, List<PersonalGoal> goals) {
        if (email == null) throw new IllegalArgumentException("Email is required");
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash is required");
        }
        if (focus == null) throw new IllegalArgumentException("Focus is required");
        if (status == null) throw new IllegalArgumentException("Account status is required");
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.focus = focus;
        this.age = age;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.mainSport = mainSport == null ? "" : mainSport.trim();
        this.trainingRoutine = trainingRoutine == null ? "" : trainingRoutine.trim();
        this.physicalHistory = physicalHistory == null ? "" : physicalHistory.trim();
        this.status = status;
        this.goals = new ArrayList<>(goals == null ? List.of() : goals);
    }

    public void updateProfile(DisplayName displayName, Focus focus, Integer age, Double weightKg, Double heightCm,
                              String mainSport, String trainingRoutine, String physicalHistory) {
        if (displayName == null) throw new IllegalArgumentException("Full name is required");
        if (focus == null) throw new IllegalArgumentException("Focus is required");
        this.displayName = displayName;
        this.focus = focus;
        this.age = age;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.mainSport = mainSport == null ? "" : mainSport.trim();
        this.trainingRoutine = trainingRoutine == null ? "" : trainingRoutine.trim();
        this.physicalHistory = physicalHistory == null ? "" : physicalHistory.trim();
    }

    public void changePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash is required");
        }
        this.passwordHash = newPasswordHash;
    }

    public void disable() {
        this.status = AccountStatus.DISABLED;
    }

    public void addGoal(String description) {
        goals.add(new PersonalGoal(null, description, false));
    }

    public void setGoalCompleted(Long goalId, boolean completed) {
        var index = indexOfGoal(goalId);
        goals.set(index, goals.get(index).withCompleted(completed));
    }

    public void removeGoal(Long goalId) {
        goals.remove(indexOfGoal(goalId));
    }

    private int indexOfGoal(Long goalId) {
        for (int i = 0; i < goals.size(); i++) {
            if (goals.get(i).id() != null && goals.get(i).id().equals(goalId)) return i;
        }
        throw new ResourceNotFoundException("Personal goal", goalId);
    }

    public Long getId() { return id; }

    public EmailAddress getEmail() { return email; }

    public String getPasswordHash() { return passwordHash; }

    public DisplayName getDisplayName() { return displayName; }

    public Focus getFocus() { return focus; }

    public Integer getAge() { return age; }

    public Double getWeightKg() { return weightKg; }

    public Double getHeightCm() { return heightCm; }

    public String getMainSport() { return mainSport; }

    public String getTrainingRoutine() { return trainingRoutine; }

    public String getPhysicalHistory() { return physicalHistory; }

    public AccountStatus getStatus() { return status; }

    public List<PersonalGoal> getGoals() { return List.copyOf(goals); }
}
