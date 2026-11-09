package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.entities.WellnessHabitEntity;

public final class WellnessHabitPersistenceMapper {

    private WellnessHabitPersistenceMapper() {
    }

    public static WellnessHabitEntity toEntity(WellnessHabit habit) {
        var entity = new WellnessHabitEntity(habit.getUserId(), habit.getName(), habit.getWeeklyTarget());
        entity.setId(habit.getId());
        return entity;
    }

    public static WellnessHabit toDomain(WellnessHabitEntity entity) {
        return new WellnessHabit(entity.getId(), entity.getUserId(), entity.getName(), entity.getWeeklyTarget());
    }
}
