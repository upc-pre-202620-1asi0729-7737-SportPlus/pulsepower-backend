package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;
import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities.WearableConnectionEntity;

public final class WearableConnectionPersistenceMapper {

    private WearableConnectionPersistenceMapper() {
    }

    public static WearableConnectionEntity toEntity(WearableConnection connection) {
        var entity = new WearableConnectionEntity(connection.getUserId(), connection.getProvider(),
                connection.getStatus(), connection.getLastSyncedAt());
        entity.setId(connection.getId());
        return entity;
    }

    public static WearableConnection toDomain(WearableConnectionEntity entity) {
        return new WearableConnection(entity.getId(), entity.getUserId(), entity.getProvider(), entity.getStatus(),
                entity.getLastSyncedAt());
    }
}
