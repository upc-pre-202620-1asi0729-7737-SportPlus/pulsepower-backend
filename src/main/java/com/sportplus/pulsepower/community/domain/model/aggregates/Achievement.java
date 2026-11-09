package com.sportplus.pulsepower.community.domain.model.aggregates;

/**
 * A catalog entry (reference data, seeded once, never user-generated). Addressed
 * internally by its stable {@code code} - rules that award achievements match on
 * it rather than on the surrogate database id.
 */
public class Achievement {

    private final Long id;
    private final String code;
    private final String name;
    private final String description;

    public Achievement(String code, String name, String description) {
        this(null, code, name, description);
    }

    public Achievement(Long id, String code, String name, String description) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Achievement code is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Achievement name is required");
        this.id = id;
        this.code = code.trim();
        this.name = name.trim();
        this.description = description == null ? "" : description.trim();
    }

    public Long getId() { return id; }

    public String getCode() { return code; }

    public String getName() { return name; }

    public String getDescription() { return description; }
}
