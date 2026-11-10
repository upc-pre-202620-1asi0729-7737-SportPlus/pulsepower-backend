package com.sportplus.pulsepower.iam.domain.model.valueobjects;

public record DisplayName(String value) {

    public DisplayName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }
        value = value.trim().replaceAll("\\s+", " ");
        if (value.length() > 100) {
            throw new IllegalArgumentException("Full name must be at most 100 characters");
        }
    }
}
