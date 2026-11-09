package com.sportplus.pulsepower.wellness.interfaces.rest;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.wellness.domain.model.commands.SaveCheckInCommand;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetCheckInsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.services.WellnessCommandService;
import com.sportplus.pulsepower.wellness.domain.services.WellnessQueryService;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.SaveCheckInResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.WellnessCheckInResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.transform.WellnessResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/wellness/check-ins", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Wellness check-ins", description = "The authenticated user's daily wellness check-ins")
public class WellnessCheckInsController {

    private final WellnessQueryService wellnessQueryService;
    private final WellnessCommandService wellnessCommandService;
    private final CurrentUserProvider currentUserProvider;

    public WellnessCheckInsController(WellnessQueryService wellnessQueryService,
                                      WellnessCommandService wellnessCommandService,
                                      CurrentUserProvider currentUserProvider) {
        this.wellnessQueryService = wellnessQueryService;
        this.wellnessCommandService = wellnessCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "List the authenticated user's check-ins")
    public ResponseEntity<List<WellnessCheckInResource>> getCheckIns() {
        var userId = currentUserProvider.requireCurrentUserId();
        var checkIns = wellnessQueryService.handle(new GetCheckInsByUserIdQuery(userId)).stream()
                .map(WellnessResourceAssembler::toResource).toList();
        return ResponseEntity.ok(checkIns);
    }

    @PostMapping
    @Operation(summary = "Save (create or replace) the check-in for a date")
    public ResponseEntity<WellnessCheckInResource> saveCheckIn(@Valid @RequestBody SaveCheckInResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var checkIn = wellnessCommandService.handle(new SaveCheckInCommand(userId, resource.date(), resource.mood(),
                resource.stress(), resource.discomfort(), resource.notes()));
        return ResponseEntity.ok(WellnessResourceAssembler.toResource(checkIn));
    }
}
