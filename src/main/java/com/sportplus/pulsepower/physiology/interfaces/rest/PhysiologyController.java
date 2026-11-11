package com.sportplus.pulsepower.physiology.interfaces.rest;

import com.sportplus.pulsepower.physiology.domain.model.commands.ConnectWearableCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.DisconnectWearableCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.ReviewAlertCommand;
import com.sportplus.pulsepower.physiology.domain.model.commands.SyncWearableDataCommand;
import com.sportplus.pulsepower.physiology.domain.model.queries.GetPhysiologicalOverviewByUserIdQuery;
import com.sportplus.pulsepower.physiology.domain.services.PhysiologyCommandService;
import com.sportplus.pulsepower.physiology.domain.services.PhysiologyQueryService;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.ConnectWearableResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.GuidanceAlertResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.PhysiologicalOverviewResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.WearableConnectionResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.transform.PhysiologyResourceAssembler;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/physiology", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Physiology", description = "Wearable connection, simulated recovery readings and guidance alerts")
public class PhysiologyController {

    private final PhysiologyQueryService physiologyQueryService;
    private final PhysiologyCommandService physiologyCommandService;
    private final CurrentUserProvider currentUserProvider;

    public PhysiologyController(PhysiologyQueryService physiologyQueryService,
                                PhysiologyCommandService physiologyCommandService,
                                CurrentUserProvider currentUserProvider) {
        this.physiologyQueryService = physiologyQueryService;
        this.physiologyCommandService = physiologyCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/overview")
    @Operation(summary = "Get the authenticated user's physiological overview")
    public ResponseEntity<PhysiologicalOverviewResource> getOverview() {
        var userId = currentUserProvider.requireCurrentUserId();
        var overview = physiologyQueryService.handle(new GetPhysiologicalOverviewByUserIdQuery(userId));
        return ResponseEntity.ok(PhysiologyResourceAssembler.toResource(overview));
    }

    @PostMapping("/connection")
    @Operation(summary = "Connect a wearable provider")
    public ResponseEntity<WearableConnectionResource> connect(@Valid @RequestBody ConnectWearableResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var connection = physiologyCommandService.handle(new ConnectWearableCommand(userId, resource.provider()));
        return ResponseEntity.ok(PhysiologyResourceAssembler.toResource(connection));
    }

    @DeleteMapping("/connection")
    @Operation(summary = "Disconnect the current wearable provider")
    public ResponseEntity<WearableConnectionResource> disconnect() {
        var userId = currentUserProvider.requireCurrentUserId();
        var connection = physiologyCommandService.handle(new DisconnectWearableCommand(userId));
        return ResponseEntity.ok(PhysiologyResourceAssembler.toResource(connection));
    }

    @PostMapping("/sync")
    @Operation(summary = "Sync simulated readings from the connected wearable")
    public ResponseEntity<WearableConnectionResource> sync() {
        var userId = currentUserProvider.requireCurrentUserId();
        var connection = physiologyCommandService.handle(new SyncWearableDataCommand(userId));
        return ResponseEntity.ok(PhysiologyResourceAssembler.toResource(connection));
    }

    @PostMapping("/alerts/{alertId}/review")
    @Operation(summary = "Mark a guidance alert as reviewed")
    public ResponseEntity<GuidanceAlertResource> reviewAlert(@PathVariable Long alertId) {
        var userId = currentUserProvider.requireCurrentUserId();
        var alert = physiologyCommandService.handle(new ReviewAlertCommand(userId, alertId));
        return ResponseEntity.ok(PhysiologyResourceAssembler.toResource(alert));
    }
}
