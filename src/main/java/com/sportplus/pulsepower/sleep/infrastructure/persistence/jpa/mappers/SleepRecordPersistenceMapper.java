package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.entities.SleepRecordEntity;

public final class SleepRecordPersistenceMapper {

    private SleepRecordPersistenceMapper() {
    }

    public static SleepRecordEntity toEntity(SleepRecord record) {
        var entity = new SleepRecordEntity(record.getUserId(), record.getStartedAt(), record.getEndedAt(),
                record.getSource(), record.getSleepScore(), record.getNotes());
        entity.setId(record.getId());
        return entity;
    }

    public static SleepRecord toDomain(SleepRecordEntity entity) {
        return new SleepRecord(entity.getId(), entity.getUserId(), entity.getStartedAt(), entity.getEndedAt(),
                entity.getSource(), entity.getSleepScore(), entity.getNotes());
    }
}
