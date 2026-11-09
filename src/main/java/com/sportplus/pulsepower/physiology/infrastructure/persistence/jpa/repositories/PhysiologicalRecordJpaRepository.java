package com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.repositories;

import com.sportplus.pulsepower.physiology.infrastructure.persistence.jpa.entities.PhysiologicalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhysiologicalRecordJpaRepository extends JpaRepository<PhysiologicalRecordEntity, Long> {

    List<PhysiologicalRecordEntity> findAllByUserIdOrderByRecordedAtDesc(Long userId);
}
