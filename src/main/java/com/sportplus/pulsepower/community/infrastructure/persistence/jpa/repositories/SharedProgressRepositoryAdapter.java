package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.domain.repositories.SharedProgressRepository;
import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.mappers.SharedProgressPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SharedProgressRepositoryAdapter implements SharedProgressRepository {

    private final SharedProgressJpaRepository jpaRepository;

    public SharedProgressRepositoryAdapter(SharedProgressJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SharedProgress save(SharedProgress progress) {
        return SharedProgressPersistenceMapper.toDomain(
                jpaRepository.save(SharedProgressPersistenceMapper.toEntity(progress)));
    }

    @Override
    public Optional<SharedProgress> findById(Long id) {
        return jpaRepository.findById(id).map(SharedProgressPersistenceMapper::toDomain);
    }

    @Override
    public List<SharedProgress> findAllByUserIdOrderByCreatedAtDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(SharedProgressPersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
