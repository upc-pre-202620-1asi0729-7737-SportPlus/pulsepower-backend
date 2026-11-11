package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;
import com.sportplus.pulsepower.physiology.domain.repositories.GuidanceAlertRepository;
import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.mappers.GuidanceAlertPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GuidanceAlertRepositoryAdapter implements GuidanceAlertRepository {

    private final GuidanceAlertJpaRepository jpaRepository;

    public GuidanceAlertRepositoryAdapter(GuidanceAlertJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public GuidanceAlert save(GuidanceAlert alert) {
        return GuidanceAlertPersistenceMapper.toDomain(
                jpaRepository.save(GuidanceAlertPersistenceMapper.toEntity(alert)));
    }

    @Override
    public Optional<GuidanceAlert> findById(Long id) {
        return jpaRepository.findById(id).map(GuidanceAlertPersistenceMapper::toDomain);
    }

    @Override
    public List<GuidanceAlert> findAllByUserIdOrderByIssuedAtDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByIssuedAtDesc(userId).stream()
                .map(GuidanceAlertPersistenceMapper::toDomain).toList();
    }
}
