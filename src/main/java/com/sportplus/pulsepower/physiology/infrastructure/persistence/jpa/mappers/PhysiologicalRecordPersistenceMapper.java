package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities.PhysiologicalRecordEntity;

public final class PhysiologicalRecordPersistenceMapper {

    private PhysiologicalRecordPersistenceMapper() {
    }

    public static PhysiologicalRecordEntity toEntity(PhysiologicalRecord record) {
        var entity = new PhysiologicalRecordEntity(record.getUserId(), record.getMetric(), record.getValue(),
                record.getUnit(), record.getRecordedAt());
        entity.setId(record.getId());
        return entity;
    }

    public static PhysiologicalRecord toDomain(PhysiologicalRecordEntity entity) {
        return new PhysiologicalRecord(entity.getId(), entity.getUserId(), entity.getMetric(), entity.getValue(),
                entity.getUnit(), entity.getRecordedAt());
    }
}
