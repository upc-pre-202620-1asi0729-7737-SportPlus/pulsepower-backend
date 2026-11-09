package com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessCheckInRepository;
import com.sportplus.pulsepower.wellness.infrastructure.persistence.jpa.mappers.WellnessCheckInPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class WellnessCheckInRepositoryAdapter implements WellnessCheckInRepository {

    private final WellnessCheckInJpaRepository jpaRepository;

    public WellnessCheckInRepositoryAdapter(WellnessCheckInJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public WellnessCheckIn save(WellnessCheckIn checkIn) {
        return WellnessCheckInPersistenceMapper.toDomain(
                jpaRepository.save(WellnessCheckInPersistenceMapper.toEntity(checkIn)));
    }

    @Override
    public Optional<WellnessCheckIn> findByUserIdAndDate(Long userId, LocalDate date) {
        return jpaRepository.findByUserIdAndDate(userId, date).map(WellnessCheckInPersistenceMapper::toDomain);
    }

    @Override
    public List<WellnessCheckIn> findAllByUserIdOrderByDateDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByDateDesc(userId).stream()
                .map(WellnessCheckInPersistenceMapper::toDomain).toList();
    }
}
