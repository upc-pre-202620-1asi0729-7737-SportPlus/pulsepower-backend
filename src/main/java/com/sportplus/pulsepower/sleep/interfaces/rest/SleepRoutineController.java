package com.sportplus.pulsepower.sleep.interfaces.rest;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.sleep.domain.model.commands.SetSleepRoutineCommand;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRoutineByUserIdQuery;
import com.sportplus.pulsepower.sleep.domain.services.SleepCommandService;
import com.sportplus.pulsepower.sleep.domain.services.SleepQueryService;
import com.sportplus.pulsepower.sleep.interfaces.rest.resources.SetSleepRoutineResource;
import com.sportplus.pulsepower.sleep.interfaces.rest.resources.SleepRoutineResource;
import com.sportplus.pulsepower.sleep.interfaces.rest.transform.SleepResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/sleep/routine", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sleep routine", description = "The authenticated user's bedtime routine")
public class SleepRoutineController {

    private final SleepQueryService sleepQueryService;
    private final SleepCommandService sleepCommandService;
    private final CurrentUserProvider currentUserProvider;

    public SleepRoutineController(SleepQueryService sleepQueryService, SleepCommandService sleepCommandService,
                                  CurrentUserProvider currentUserProvider) {
        this.sleepQueryService = sleepQueryService;
        this.sleepCommandService = sleepCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "Get the authenticated user's sleep routine")
    public ResponseEntity<SleepRoutineResource> getRoutine() {
        var userId = currentUserProvider.requireCurrentUserId();
        return sleepQueryService.handle(new GetSleepRoutineByUserIdQuery(userId))
                .map(routine -> ResponseEntity.ok(SleepResourceAssembler.toResource(routine)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping
    @Operation(summary = "Set the authenticated user's sleep routine")
    public ResponseEntity<SleepRoutineResource> setRoutine(@Valid @RequestBody SetSleepRoutineResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var routine = sleepCommandService.handle(new SetSleepRoutineCommand(userId, resource.bedtime(),
                resource.wakeTime(), resource.targetHours(), resource.reminderEnabled()));
        return ResponseEntity.ok(SleepResourceAssembler.toResource(routine));
    }
}
