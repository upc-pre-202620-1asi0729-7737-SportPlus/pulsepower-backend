package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.domain.repositories.PhysiologicalRecordRepository;
import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.mappers.PhysiologicalRecordPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhysiologicalRecordRepositoryAdapter implements PhysiologicalRecordRepository {

    private final PhysiologicalRecordJpaRepository jpaRepository;

    public PhysiologicalRecordRepositoryAdapter(PhysiologicalRecordJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PhysiologicalRecord save(PhysiologicalRecord record) {
        return PhysiologicalRecordPersistenceMapper.toDomain(
                jpaRepository.save(PhysiologicalRecordPersistenceMapper.toEntity(record)));
    }

    @Override
    public List<PhysiologicalRecord> findAllByUserIdOrderByRecordedAtDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByRecordedAtDesc(userId).stream()
                .map(PhysiologicalRecordPersistenceMapper::toDomain).toList();
    }
}
