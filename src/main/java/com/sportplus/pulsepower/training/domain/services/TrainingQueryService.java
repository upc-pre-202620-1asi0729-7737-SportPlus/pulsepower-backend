package com.sportplus.pulsepower.training.domain.services;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.model.queries.GetSessionsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.model.queries.GetTrainingPlanByUserIdQuery;

import java.util.List;

public interface TrainingQueryService {

    TrainingPlan handle(GetTrainingPlanByUserIdQuery query);

    List<TrainingSession> handle(GetSessionsByUserIdQuery query);
}
