package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRecordRepository;
import com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.mappers.SleepRecordPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SleepRecordRepositoryAdapter implements SleepRecordRepository {

    private final SleepRecordJpaRepository jpaRepository;

    public SleepRecordRepositoryAdapter(SleepRecordJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SleepRecord save(SleepRecord record) {
        return SleepRecordPersistenceMapper.toDomain(
                jpaRepository.save(SleepRecordPersistenceMapper.toEntity(record)));
    }

    @Override
    public Optional<SleepRecord> findById(Long id) {
        return jpaRepository.findById(id).map(SleepRecordPersistenceMapper::toDomain);
    }

    @Override
    public List<SleepRecord> findAllByUserIdOrderByStartedAtDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByStartedAtDesc(userId).stream()
                .map(SleepRecordPersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
