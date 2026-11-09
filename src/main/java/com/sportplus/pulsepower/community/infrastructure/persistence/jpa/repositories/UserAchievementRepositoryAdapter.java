package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.community.domain.model.aggregates.UserAchievement;
import com.sportplus.pulsepower.community.domain.repositories.UserAchievementRepository;
import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.mappers.UserAchievementPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAchievementRepositoryAdapter implements UserAchievementRepository {

    private final UserAchievementJpaRepository jpaRepository;

    public UserAchievementRepositoryAdapter(UserAchievementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserAchievement save(UserAchievement userAchievement) {
        return UserAchievementPersistenceMapper.toDomain(
                jpaRepository.save(UserAchievementPersistenceMapper.toEntity(userAchievement)));
    }

    @Override
    public List<UserAchievement> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream()
                .map(UserAchievementPersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean existsByUserIdAndAchievementId(Long userId, Long achievementId) {
        return jpaRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }
}
