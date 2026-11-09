package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.valueobjects.PlannedActivity;
import com.sportplus.pulsepower.training.domain.model.valueobjects.TrainingPeriod;
import com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities.PlannedActivityEntity;
import com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities.TrainingPlanEntity;

public final class TrainingPlanPersistenceMapper {

    private TrainingPlanPersistenceMapper() {
    }

    public static TrainingPlanEntity toEntity(TrainingPlan plan) {
        var period = plan.getPeriod();
        var entity = new TrainingPlanEntity(plan.getUserId(), plan.getName(),
                period == null ? null : period.from(), period == null ? null : period.to());
        entity.setId(plan.getId());
        entity.setActivities(plan.getActivities().stream()
                .map(activity -> new PlannedActivityEntity(activity.id(), activity.date(), activity.startTime(),
                        activity.activity(), activity.durationMinutes(), activity.intensity(), activity.kind(),
                        activity.status()))
                .toList());
        return entity;
    }

    public static TrainingPlan toDomain(TrainingPlanEntity entity) {
        var period = entity.getPeriodFrom() == null ? null
                : new TrainingPeriod(entity.getPeriodFrom(), entity.getPeriodTo());
        var activities = entity.getActivities().stream()
                .map(activity -> new PlannedActivity(activity.getId(), activity.getDate(), activity.getStartTime(),
                        activity.getActivity(), activity.getDurationMinutes(), activity.getIntensity(),
                        activity.getKind(), activity.getStatus()))
                .toList();
        return new TrainingPlan(entity.getId(), entity.getUserId(), entity.getName(), period, activities);
    }
}
