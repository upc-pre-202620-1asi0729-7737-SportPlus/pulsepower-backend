package com.sportplus.pulsepower.iam.domain.model.valueobjects;

public record PersonalGoal(Long id, String description, boolean completed) {

    public PersonalGoal {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Goal description is required");
        }
        description = description.trim();
        if (description.length() > 200) {
            throw new IllegalArgumentException("Goal description must be at most 200 characters");
        }
    }

    public PersonalGoal withId(Long newId) {
        return new PersonalGoal(newId, description, completed);
    }

    public PersonalGoal withCompleted(boolean newCompleted) {
        return new PersonalGoal(id, description, newCompleted);
    }
}
