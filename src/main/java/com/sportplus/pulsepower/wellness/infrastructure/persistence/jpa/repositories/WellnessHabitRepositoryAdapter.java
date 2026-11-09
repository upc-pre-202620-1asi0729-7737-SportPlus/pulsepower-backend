package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessHabitRepository;
import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.mappers.WellnessHabitPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WellnessHabitRepositoryAdapter implements WellnessHabitRepository {

    private final WellnessHabitJpaRepository jpaRepository;

    public WellnessHabitRepositoryAdapter(WellnessHabitJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public WellnessHabit save(WellnessHabit habit) {
        return WellnessHabitPersistenceMapper.toDomain(
                jpaRepository.save(WellnessHabitPersistenceMapper.toEntity(habit)));
    }

    @Override
    public Optional<WellnessHabit> findById(Long id) {
        return jpaRepository.findById(id).map(WellnessHabitPersistenceMapper::toDomain);
    }

    @Override
    public List<WellnessHabit> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(WellnessHabitPersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
