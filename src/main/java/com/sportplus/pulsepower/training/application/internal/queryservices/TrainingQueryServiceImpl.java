package com.sportplus.pulsepower.training.application.internal.queryservices;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.model.queries.GetSessionsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.model.queries.GetTrainingPlanByUserIdQuery;
import com.sportplus.pulsepower.training.domain.repositories.TrainingPlanRepository;
import com.sportplus.pulsepower.training.domain.repositories.TrainingSessionRepository;
import com.sportplus.pulsepower.training.domain.services.TrainingQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrainingQueryServiceImpl implements TrainingQueryService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public TrainingQueryServiceImpl(TrainingPlanRepository trainingPlanRepository,
                                    TrainingSessionRepository trainingSessionRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Override
    public TrainingPlan handle(GetTrainingPlanByUserIdQuery query) {
        return trainingPlanRepository.findByUserId(query.userId())
                .orElseGet(() -> new TrainingPlan(query.userId(), "Your training plan"));
    }

    @Override
    public List<TrainingSession> handle(GetSessionsByUserIdQuery query) {
        return trainingSessionRepository.findAllByUserIdOrderByDateDesc(query.userId());
    }
}
