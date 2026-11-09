package com.sportplus.pulsepower.training.application.internal.commandservices;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.model.commands.*;
import com.sportplus.pulsepower.training.domain.repositories.TrainingPlanRepository;
import com.sportplus.pulsepower.training.domain.repositories.TrainingSessionRepository;
import com.sportplus.pulsepower.training.domain.services.TrainingCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainingCommandServiceImpl implements TrainingCommandService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final CurrentUserProvider currentUserProvider;

    public TrainingCommandServiceImpl(TrainingPlanRepository trainingPlanRepository,
                                      TrainingSessionRepository trainingSessionRepository,
                                      CurrentUserProvider currentUserProvider) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingSessionRepository = trainingSessionRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public TrainingPlan handle(SetPlanPeriodCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Training plan");
        var plan = findOrCreatePlan(command.userId());
        plan.setPeriod(command.from(), command.to());
        return trainingPlanRepository.save(plan);
    }

    @Override
    public TrainingPlan handle(AddPlannedActivityCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Training plan");
        var plan = findOrCreatePlan(command.userId());
        plan.addActivity(command.date(), command.startTime(), command.activity(), command.durationMinutes(),
                command.intensity(), command.kind());
        return trainingPlanRepository.save(plan);
    }

    @Override
    public TrainingPlan handle(CancelPlannedActivityCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Training plan");
        var plan = trainingPlanRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Training plan", command.userId()));
        plan.cancelActivity(command.activityId());
        return trainingPlanRepository.save(plan);
    }

    @Override
    public TrainingSession handle(RecordTrainingSessionCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Training session");
        if (command.plannedActivityId() != null) {
            var plan = trainingPlanRepository.findByUserId(command.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Training plan", command.userId()));
            plan.completeActivity(command.plannedActivityId());
            trainingPlanRepository.save(plan);
        }
        var session = new TrainingSession(command.userId(), command.date(), command.activity(),
                command.durationMinutes(), command.perceivedEffort(), command.notes(), command.plannedActivityId());
        return trainingSessionRepository.save(session);
    }

    @Override
    public void handle(DeleteTrainingSessionCommand command) {
        var session = trainingSessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Training session", command.sessionId()));
        currentUserProvider.ensureOwnership(session.getUserId(), "Training session");
        trainingSessionRepository.deleteById(command.sessionId());
    }

    @Override
    public TrainingPlan handle(RescheduleActivityCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Training plan");
        var plan = findOrCreatePlan(command.userId());
        plan.rescheduleActivity(command.activityId(), command.date(), command.startTime());
        return trainingPlanRepository.save(plan);
    }

    private TrainingPlan findOrCreatePlan(Long userId) {
        return trainingPlanRepository.findByUserId(userId)
                .orElseGet(() -> new TrainingPlan(userId, "Your training plan"));
    }
}
