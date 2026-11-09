package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities.WellnessCheckInEntity;

public final class WellnessCheckInPersistenceMapper {

    private WellnessCheckInPersistenceMapper() {
    }

    public static WellnessCheckInEntity toEntity(WellnessCheckIn checkIn) {
        var entity = new WellnessCheckInEntity(checkIn.getUserId(), checkIn.getDate(), checkIn.getMood(),
                checkIn.getStress(), checkIn.isDiscomfort(), checkIn.getNotes());
        entity.setId(checkIn.getId());
        return entity;
    }

    public static WellnessCheckIn toDomain(WellnessCheckInEntity entity) {
        return new WellnessCheckIn(entity.getId(), entity.getUserId(), entity.getDate(), entity.getMood(),
                entity.getStress(), entity.isDiscomfort(), entity.getNotes());
    }
}
