package com.sportplus.pulsepower.training.domain.services;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.model.commands.*;

public interface TrainingCommandService {

    TrainingPlan handle(RescheduleActivityCommand command);

    TrainingPlan handle(SetPlanPeriodCommand command);

    TrainingPlan handle(AddPlannedActivityCommand command);

    TrainingPlan handle(CancelPlannedActivityCommand command);

    TrainingSession handle(RecordTrainingSessionCommand command);

    void handle(DeleteTrainingSessionCommand command);
}
