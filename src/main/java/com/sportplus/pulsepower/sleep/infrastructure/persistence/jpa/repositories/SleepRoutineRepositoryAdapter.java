package com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRoutineRepository;
import com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.entities.SleepRoutineEntity;
import com.sportplus.pulsepower.sleep.infrastructure.persistence.jpa.mappers.SleepRoutinePersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SleepRoutineRepositoryAdapter implements SleepRoutineRepository {

    private final SleepRoutineJpaRepository jpaRepository;

    public SleepRoutineRepositoryAdapter(SleepRoutineJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SleepRoutine save(SleepRoutine routine) {
        var existing = jpaRepository.findByUserId(routine.getUserId());
        var entity = SleepRoutinePersistenceMapper.toEntity(routine);
        existing.ifPresent(current -> entity.setId(current.getId()));
        return SleepRoutinePersistenceMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<SleepRoutine> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).map(SleepRoutinePersistenceMapper::toDomain);
    }
}
