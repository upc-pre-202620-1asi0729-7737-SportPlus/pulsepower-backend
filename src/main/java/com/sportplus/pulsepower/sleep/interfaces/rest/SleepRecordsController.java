package com.sportplus.pulsepower.sleep.interfaces.rest;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.sleep.domain.model.commands.AddSleepRecordCommand;
import com.sportplus.pulsepower.sleep.domain.model.commands.DeleteSleepRecordCommand;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRecordsByUserIdQuery;
import com.sportplus.pulsepower.sleep.domain.services.SleepCommandService;
import com.sportplus.pulsepower.sleep.domain.services.SleepQueryService;
import com.sportplus.pulsepower.sleep.interfaces.rest.resources.AddSleepRecordResource;
import com.sportplus.pulsepower.sleep.interfaces.rest.resources.SleepRecordResource;
import com.sportplus.pulsepower.sleep.interfaces.rest.transform.SleepResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/sleep/records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sleep records", description = "The authenticated user's sleep records")
public class SleepRecordsController {

    private final SleepQueryService sleepQueryService;
    private final SleepCommandService sleepCommandService;
    private final CurrentUserProvider currentUserProvider;

    public SleepRecordsController(SleepQueryService sleepQueryService, SleepCommandService sleepCommandService,
                                  CurrentUserProvider currentUserProvider) {
        this.sleepQueryService = sleepQueryService;
        this.sleepCommandService = sleepCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "List the authenticated user's sleep records")
    public ResponseEntity<List<SleepRecordResource>> getRecords() {
        var userId = currentUserProvider.requireCurrentUserId();
        var records = sleepQueryService.handle(new GetSleepRecordsByUserIdQuery(userId)).stream()
                .map(SleepResourceAssembler::toResource).toList();
        return ResponseEntity.ok(records);
    }

    @PostMapping
    @Operation(summary = "Add a sleep record")
    public ResponseEntity<SleepRecordResource> addRecord(@Valid @RequestBody AddSleepRecordResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var record = sleepCommandService.handle(new AddSleepRecordCommand(userId, resource.startedAt(),
                resource.endedAt(), SleepResourceAssembler.toSource(resource.source()), resource.sleepScore(),
                resource.notes()));
        return ResponseEntity.ok(SleepResourceAssembler.toResource(record));
    }

    @PutMapping("/{recordId}")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<SleepRecordResource> replaceRecord(@PathVariable Long recordId,
            @Valid @RequestBody AddSleepRecordResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        sleepCommandService.handle(new DeleteSleepRecordCommand(userId, recordId));
        return addRecord(resource);
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "Delete a sleep record")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long recordId) {
        var userId = currentUserProvider.requireCurrentUserId();
        sleepCommandService.handle(new DeleteSleepRecordCommand(userId, recordId));
        return ResponseEntity.noContent().build();
    }
}
