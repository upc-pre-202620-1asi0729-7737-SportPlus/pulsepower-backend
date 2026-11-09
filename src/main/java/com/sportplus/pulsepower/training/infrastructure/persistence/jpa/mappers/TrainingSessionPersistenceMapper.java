package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.infrastructure.persistence.jpa.entities.TrainingSessionEntity;

public final class TrainingSessionPersistenceMapper {

    private TrainingSessionPersistenceMapper() {
    }

    public static TrainingSessionEntity toEntity(TrainingSession session) {
        var entity = new TrainingSessionEntity(session.getUserId(), session.getDate(), session.getActivity(),
                session.getDurationMinutes(), session.getPerceivedEffort(), session.getNotes(),
                session.getPlannedActivityId());
        entity.setId(session.getId());
        return entity;
    }

    public static TrainingSession toDomain(TrainingSessionEntity entity) {
        return new TrainingSession(entity.getId(), entity.getUserId(), entity.getDate(), entity.getActivity(),
                entity.getDurationMinutes(), entity.getPerceivedEffort(), entity.getNotes(),
                entity.getPlannedActivityId());
    }
}
