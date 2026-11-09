package com.sportplus.pulsepower.training.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.repositories.TrainingSessionRepository;
import com.sportplus.pulsepower.training.infrastructure.persistence.jpa.mappers.TrainingSessionPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingSessionRepositoryAdapter implements TrainingSessionRepository {

    private final TrainingSessionJpaRepository jpaRepository;

    public TrainingSessionRepositoryAdapter(TrainingSessionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TrainingSession save(TrainingSession session) {
        return TrainingSessionPersistenceMapper.toDomain(
                jpaRepository.save(TrainingSessionPersistenceMapper.toEntity(session)));
    }

    @Override
    public Optional<TrainingSession> findById(Long id) {
        return jpaRepository.findById(id).map(TrainingSessionPersistenceMapper::toDomain);
    }

    @Override
    public List<TrainingSession> findAllByUserIdOrderByDateDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByDateDesc(userId).stream()
                .map(TrainingSessionPersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
