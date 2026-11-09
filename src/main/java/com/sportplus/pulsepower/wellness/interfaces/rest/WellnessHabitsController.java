package com.sportplus.pulsepower.wellness.interfaces.rest;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.wellness.domain.model.commands.CreateHabitCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.DeleteHabitCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.ToggleHabitLogCommand;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetHabitLogsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetHabitsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.services.WellnessCommandService;
import com.sportplus.pulsepower.wellness.domain.services.WellnessQueryService;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.CreateHabitResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.HabitLogResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.ToggleHabitLogResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.WellnessHabitResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.transform.WellnessResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/wellness/habits", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Wellness habits", description = "The authenticated user's habits and their daily logs")
public class WellnessHabitsController {

    private final WellnessQueryService wellnessQueryService;
    private final WellnessCommandService wellnessCommandService;
    private final CurrentUserProvider currentUserProvider;

    public WellnessHabitsController(WellnessQueryService wellnessQueryService,
                                    WellnessCommandService wellnessCommandService,
                                    CurrentUserProvider currentUserProvider) {
        this.wellnessQueryService = wellnessQueryService;
        this.wellnessCommandService = wellnessCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "List the authenticated user's habits")
    public ResponseEntity<List<WellnessHabitResource>> getHabits() {
        var userId = currentUserProvider.requireCurrentUserId();
        var habits = wellnessQueryService.handle(new GetHabitsByUserIdQuery(userId)).stream()
                .map(WellnessResourceAssembler::toResource).toList();
        return ResponseEntity.ok(habits);
    }

    @PostMapping
    @Operation(summary = "Create a habit")
    public ResponseEntity<WellnessHabitResource> createHabit(@Valid @RequestBody CreateHabitResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var habit = wellnessCommandService.handle(new CreateHabitCommand(userId, resource.name(), resource.weeklyTarget()));
        return ResponseEntity.ok(WellnessResourceAssembler.toResource(habit));
    }

    @DeleteMapping("/{habitId}")
    @Operation(summary = "Delete a habit and its logs")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long habitId) {
        var userId = currentUserProvider.requireCurrentUserId();
        wellnessCommandService.handle(new DeleteHabitCommand(userId, habitId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/logs")
    @Operation(summary = "List the authenticated user's habit logs")
    public ResponseEntity<List<HabitLogResource>> getLogs() {
        var userId = currentUserProvider.requireCurrentUserId();
        var logs = wellnessQueryService.handle(new GetHabitLogsByUserIdQuery(userId)).stream()
                .map(WellnessResourceAssembler::toResource).toList();
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/{habitId}/logs")
    @Operation(summary = "Toggle whether a habit is logged for a date")
    public ResponseEntity<Map<String, Boolean>> toggleLog(@PathVariable Long habitId,
                                                           @Valid @RequestBody ToggleHabitLogResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var logged = wellnessCommandService.handle(new ToggleHabitLogCommand(userId, habitId, resource.date()));
        return ResponseEntity.ok(Map.of("logged", logged));
    }
}
