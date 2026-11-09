package com.sportplus.pulsepower.training.interfaces.rest;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.training.domain.model.commands.DeleteTrainingSessionCommand;
import com.sportplus.pulsepower.training.domain.model.commands.RecordTrainingSessionCommand;
import com.sportplus.pulsepower.training.domain.model.queries.GetSessionsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.services.TrainingCommandService;
import com.sportplus.pulsepower.training.domain.services.TrainingQueryService;
import com.sportplus.pulsepower.training.interfaces.rest.resources.RecordTrainingSessionResource;
import com.sportplus.pulsepower.training.interfaces.rest.resources.TrainingSessionResource;
import com.sportplus.pulsepower.training.interfaces.rest.transform.TrainingResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/training/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Training sessions", description = "The authenticated user's completed training sessions")
public class TrainingSessionsController {

    private final TrainingQueryService trainingQueryService;
    private final TrainingCommandService trainingCommandService;
    private final CurrentUserProvider currentUserProvider;

    public TrainingSessionsController(TrainingQueryService trainingQueryService,
                                      TrainingCommandService trainingCommandService,
                                      CurrentUserProvider currentUserProvider) {
        this.trainingQueryService = trainingQueryService;
        this.trainingCommandService = trainingCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "List the authenticated user's training sessions")
    public ResponseEntity<List<TrainingSessionResource>> getSessions() {
        var userId = currentUserProvider.requireCurrentUserId();
        var sessions = trainingQueryService.handle(new GetSessionsByUserIdQuery(userId)).stream()
                .map(TrainingResourceAssembler::toResource).toList();
        return ResponseEntity.ok(sessions);
    }

    @PostMapping
    @Operation(summary = "Record a training session")
    public ResponseEntity<TrainingSessionResource> recordSession(@Valid @RequestBody RecordTrainingSessionResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var session = trainingCommandService.handle(new RecordTrainingSessionCommand(userId, resource.date(),
                resource.activity(), resource.durationMinutes(), resource.perceivedEffort(), resource.notes(),
                resource.plannedActivityId()));
        return ResponseEntity.ok(TrainingResourceAssembler.toResource(session));
    }

    @DeleteMapping("/{sessionId}")
    @Operation(summary = "Delete a training session")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        var userId = currentUserProvider.requireCurrentUserId();
        trainingCommandService.handle(new DeleteTrainingSessionCommand(userId, sessionId));
        return ResponseEntity.noContent().build();
    }
}
