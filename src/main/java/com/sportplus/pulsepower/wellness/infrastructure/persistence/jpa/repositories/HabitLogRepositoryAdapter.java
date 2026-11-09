package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.domain.repositories.HabitLogRepository;
import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.mappers.HabitLogPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class HabitLogRepositoryAdapter implements HabitLogRepository {

    private final HabitLogJpaRepository jpaRepository;

    public HabitLogRepositoryAdapter(HabitLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public HabitLog save(HabitLog log) {
        return HabitLogPersistenceMapper.toDomain(jpaRepository.save(HabitLogPersistenceMapper.toEntity(log)));
    }

    @Override
    public Optional<HabitLog> findByHabitIdAndDate(Long habitId, LocalDate date) {
        return jpaRepository.findByHabitIdAndDate(habitId, date).map(HabitLogPersistenceMapper::toDomain);
    }

    @Override
    public List<HabitLog> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(HabitLogPersistenceMapper::toDomain).toList();
    }

    @Override
    public void delete(HabitLog log) {
        jpaRepository.deleteById(log.getId());
    }

    @Override
    public void deleteAllByHabitId(Long habitId) {
        jpaRepository.deleteAllByHabitId(habitId);
    }
}
