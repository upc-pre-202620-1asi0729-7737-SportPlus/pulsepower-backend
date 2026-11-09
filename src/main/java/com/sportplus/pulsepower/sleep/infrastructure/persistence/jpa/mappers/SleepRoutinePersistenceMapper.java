package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.entities.SleepRoutineEntity;

public final class SleepRoutinePersistenceMapper {

    private SleepRoutinePersistenceMapper() {
    }

    public static SleepRoutineEntity toEntity(SleepRoutine routine) {
        var entity = new SleepRoutineEntity(routine.getUserId(), routine.getBedtime(), routine.getWakeTime(),
                routine.getTargetHours(), routine.isReminderEnabled());
        return entity;
    }

    public static SleepRoutine toDomain(SleepRoutineEntity entity) {
        return new SleepRoutine(entity.getUserId(), entity.getBedtime(), entity.getWakeTime(),
                entity.getTargetHours(), entity.isReminderEnabled());
    }
}
