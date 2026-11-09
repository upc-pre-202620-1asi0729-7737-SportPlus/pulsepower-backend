package com.sportplus.pulsepower.physiology.domain.repositories;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;

import java.util.List;

public interface PhysiologicalRecordRepository {

    PhysiologicalRecord save(PhysiologicalRecord record);

    List<PhysiologicalRecord> findAllByUserIdOrderByRecordedAtDesc(Long userId);
}
