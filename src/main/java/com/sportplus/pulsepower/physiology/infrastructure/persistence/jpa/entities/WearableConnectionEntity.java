package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities;

import com.sportplus.pulsepower.physiology.domain.model.valueobjects.ConnectionStatus;
import com.sportplus.pulsepower.shared.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Entity(name = "WearableConnection")
public class WearableConnectionEntity extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConnectionStatus status;

    private Instant lastSyncedAt;

    protected WearableConnectionEntity() {
    }

    public WearableConnectionEntity(Long userId, String provider, ConnectionStatus status, Instant lastSyncedAt) {
        this.userId = userId;
        this.provider = provider;
        this.status = status;
        this.lastSyncedAt = lastSyncedAt;
    }

    public Long getUserId() { return userId; }

    public String getProvider() { return provider; }

    public ConnectionStatus getStatus() { return status; }

    public Instant getLastSyncedAt() { return lastSyncedAt; }
}
