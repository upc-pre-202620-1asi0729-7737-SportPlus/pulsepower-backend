package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;
import com.sportplus.pulsepower.physiology.domain.repositories.WearableConnectionRepository;
import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.mappers.WearableConnectionPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WearableConnectionRepositoryAdapter implements WearableConnectionRepository {

    private final WearableConnectionJpaRepository jpaRepository;

    public WearableConnectionRepositoryAdapter(WearableConnectionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public WearableConnection save(WearableConnection connection) {
        var existing = jpaRepository.findByUserId(connection.getUserId());
        var entity = WearableConnectionPersistenceMapper.toEntity(connection);
        existing.ifPresent(current -> entity.setId(current.getId()));
        return WearableConnectionPersistenceMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<WearableConnection> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).map(WearableConnectionPersistenceMapper::toDomain);
    }
}
