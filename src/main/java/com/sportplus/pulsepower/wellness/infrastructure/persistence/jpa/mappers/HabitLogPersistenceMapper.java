package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities.HabitLogEntity;

public final class HabitLogPersistenceMapper {

    private HabitLogPersistenceMapper() {
    }

    public static HabitLogEntity toEntity(HabitLog log) {
        var entity = new HabitLogEntity(log.getUserId(), log.getHabitId(), log.getDate());
        entity.setId(log.getId());
        return entity;
    }

    public static HabitLog toDomain(HabitLogEntity entity) {
        return new HabitLog(entity.getId(), entity.getUserId(), entity.getHabitId(), entity.getDate());
    }
}
