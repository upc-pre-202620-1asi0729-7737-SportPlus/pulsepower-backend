package com.sportplus.pulsepower.community.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.community.infrastructure.persistence.jpa.entities.SharedProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedProgressJpaRepository extends JpaRepository<SharedProgressEntity, Long> {

    List<SharedProgressEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
