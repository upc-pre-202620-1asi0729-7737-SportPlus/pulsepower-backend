package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.community.domain.model.valueobjects.Audience;
import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "SharedProgress")
public class SharedProgressEntity extends AuditableEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "shared_progress_audience", joinColumns = @JoinColumn(name = "shared_progress_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "audience", nullable = false, length = 20)
    private Set<Audience> audiences = new HashSet<>();

    protected SharedProgressEntity() {
    }

    public SharedProgressEntity(Long userId, String title, String content, Instant recordedAt, Set<Audience> audiences) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.recordedAt = recordedAt;
        this.audiences = audiences == null ? new HashSet<>() : new HashSet<>(audiences);
    }

    public Long getUserId() { return userId; }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public Instant getRecordedAt() { return recordedAt; }

    public Set<Audience> getAudiences() { return audiences; }
}
