package com.sportplus.pulsepower.training.interfaces.rest;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.training.domain.model.commands.AddPlannedActivityCommand;
import com.sportplus.pulsepower.training.domain.model.commands.CancelPlannedActivityCommand;
import com.sportplus.pulsepower.training.domain.model.commands.SetPlanPeriodCommand;
import com.sportplus.pulsepower.training.domain.model.queries.GetTrainingPlanByUserIdQuery;
import com.sportplus.pulsepower.training.domain.services.TrainingCommandService;
import com.sportplus.pulsepower.training.domain.services.TrainingQueryService;
import com.sportplus.pulsepower.training.interfaces.rest.resources.AddPlannedActivityResource;
import com.sportplus.pulsepower.training.interfaces.rest.resources.SetPlanPeriodResource;
import com.sportplus.pulsepower.training.interfaces.rest.resources.TrainingPlanResource;
import com.sportplus.pulsepower.training.interfaces.rest.transform.TrainingResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/training/plan", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Training plan", description = "The authenticated user's training plan and planned activities")
public class TrainingPlanController {

    private final TrainingQueryService trainingQueryService;
    private final TrainingCommandService trainingCommandService;
    private final CurrentUserProvider currentUserProvider;

    public TrainingPlanController(TrainingQueryService trainingQueryService, TrainingCommandService trainingCommandService,
                                  CurrentUserProvider currentUserProvider) {
        this.trainingQueryService = trainingQueryService;
        this.trainingCommandService = trainingCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    public record RescheduleResource(@jakarta.validation.constraints.NotNull java.time.LocalDate date, java.time.LocalTime startTime) {}

    @PutMapping("/activities/{activityId}")
    public ResponseEntity<TrainingPlanResource> reschedule(@PathVariable Long activityId,
            @Valid @RequestBody RescheduleResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var plan = trainingCommandService.handle(new com.sportplus.pulsepower.training.domain.model.commands.RescheduleActivityCommand(
                userId, activityId, resource.date(), resource.startTime()));
        return ResponseEntity.ok(TrainingResourceAssembler.toResource(plan));
    }

    @GetMapping
    @Operation(summary = "Get the authenticated user's training plan")
    public ResponseEntity<TrainingPlanResource> getPlan() {
        var userId = currentUserProvider.requireCurrentUserId();
        var plan = trainingQueryService.handle(new GetTrainingPlanByUserIdQuery(userId));
        return ResponseEntity.ok(TrainingResourceAssembler.toResource(plan));
    }

    @PutMapping("/period")
    @Operation(summary = "Set the training plan's period")
    public ResponseEntity<TrainingPlanResource> setPeriod(@Valid @RequestBody SetPlanPeriodResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var plan = trainingCommandService.handle(new SetPlanPeriodCommand(userId, resource.from(), resource.to()));
        return ResponseEntity.ok(TrainingResourceAssembler.toResource(plan));
    }

    @PostMapping("/activities")
    @Operation(summary = "Add a planned activity within the current period")
    public ResponseEntity<TrainingPlanResource> addActivity(@Valid @RequestBody AddPlannedActivityResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var plan = trainingCommandService.handle(new AddPlannedActivityCommand(userId, resource.date(),
                resource.startTime(), resource.activity(), resource.durationMinutes(),
                TrainingResourceAssembler.toIntensity(resource.intensity()),
                TrainingResourceAssembler.toKind(resource.kind())));
        return ResponseEntity.ok(TrainingResourceAssembler.toResource(plan));
    }

    @PostMapping("/activities/{activityId}/cancel")
    @Operation(summary = "Cancel a planned activity")
    public ResponseEntity<TrainingPlanResource> cancelActivity(@PathVariable Long activityId) {
        var userId = currentUserProvider.requireCurrentUserId();
        var plan = trainingCommandService.handle(new CancelPlannedActivityCommand(userId, activityId));
        return ResponseEntity.ok(TrainingResourceAssembler.toResource(plan));
    }
}
