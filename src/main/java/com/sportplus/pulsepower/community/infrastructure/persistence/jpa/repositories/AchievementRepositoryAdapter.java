package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.community.domain.model.aggregates.Achievement;
import com.sportplus.pulsepower.community.domain.repositories.AchievementRepository;
import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.mappers.AchievementPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AchievementRepositoryAdapter implements AchievementRepository {

    private final AchievementJpaRepository jpaRepository;

    public AchievementRepositoryAdapter(AchievementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Achievement save(Achievement achievement) {
        return AchievementPersistenceMapper.toDomain(
                jpaRepository.save(AchievementPersistenceMapper.toEntity(achievement)));
    }

    @Override
    public Optional<Achievement> findByCode(String code) {
        return jpaRepository.findByCode(code).map(AchievementPersistenceMapper::toDomain);
    }

    @Override
    public List<Achievement> findAll() {
        return jpaRepository.findAll().stream().map(AchievementPersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
