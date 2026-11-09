package com.sportplus.pulsepower.physiology.domain.model.aggregates;

import com.sportplus.pulsepower.physiology.domain.model.valueobjects.ConnectionStatus;

import java.time.Instant;

public class WearableConnection {

    private final Long id;
    private final Long userId;
    private String provider;
    private ConnectionStatus status;
    private Instant lastSyncedAt;

    public WearableConnection(Long userId, String provider) {
        this(null, userId, provider, ConnectionStatus.CONNECTED, null);
    }

    public WearableConnection(Long id, Long userId, String provider, ConnectionStatus status, Instant lastSyncedAt) {
        if (userId == null) throw new IllegalArgumentException("A wearable connection must belong to a user");
        if (provider == null || provider.isBlank()) throw new IllegalArgumentException("Provider name is required");
        if (status == null) throw new IllegalArgumentException("Connection status is required");
        this.id = id;
        this.userId = userId;
        this.provider = provider.trim();
        this.status = status;
        this.lastSyncedAt = lastSyncedAt;
    }

    public void disconnect() {
        this.status = ConnectionStatus.DISCONNECTED;
    }

    public void recordSync(Instant at) {
        this.status = ConnectionStatus.CONNECTED;
        this.lastSyncedAt = at;
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public String getProvider() { return provider; }

    public ConnectionStatus getStatus() { return status; }

    public Instant getLastSyncedAt() { return lastSyncedAt; }
}
