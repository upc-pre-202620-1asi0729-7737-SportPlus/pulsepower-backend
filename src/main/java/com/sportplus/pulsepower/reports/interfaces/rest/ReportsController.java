package com.sportplus.pulsepower.reports.interfaces.rest;

import com.sportplus.pulsepower.reports.domain.model.commands.CreateReportCommand;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportByIdQuery;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportEntriesQuery;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportsByUserIdQuery;
import com.sportplus.pulsepower.reports.domain.services.ReportCommandService;
import com.sportplus.pulsepower.reports.domain.services.ReportQueryService;
import com.sportplus.pulsepower.reports.interfaces.rest.resources.CreateReportResource;
import com.sportplus.pulsepower.reports.interfaces.rest.resources.ProgressReportResource;
import com.sportplus.pulsepower.reports.interfaces.rest.resources.ReportDocumentResource;
import com.sportplus.pulsepower.reports.interfaces.rest.resources.ReportEntryResource;
import com.sportplus.pulsepower.reports.interfaces.rest.transform.ReportResourceAssembler;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Progress reports built from the authenticated user's training, sleep, wellness and recovery history")
public class ReportsController {

    private final ReportQueryService reportQueryService;
    private final ReportCommandService reportCommandService;
    private final CurrentUserProvider currentUserProvider;

    public ReportsController(ReportQueryService reportQueryService, ReportCommandService reportCommandService,
                             CurrentUserProvider currentUserProvider) {
        this.reportQueryService = reportQueryService;
        this.reportCommandService = reportCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "List the authenticated user's generated reports")
    public ResponseEntity<List<ProgressReportResource>> getReports() {
        var userId = currentUserProvider.requireCurrentUserId();
        var reports = reportQueryService.handle(new GetReportsByUserIdQuery(userId)).stream()
                .map(ReportResourceAssembler::toResource).toList();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/entries")
    @Operation(summary = "List the entries available for a report, before generating it")
    public ResponseEntity<List<ReportEntryResource>> getEntries(@RequestParam LocalDate from,
                                                                @RequestParam LocalDate to,
                                                                @RequestParam List<String> types) {
        var userId = currentUserProvider.requireCurrentUserId();
        var parsedTypes = types.stream().map(ReportResourceAssembler::toType).toList();
        var entries = reportQueryService.handle(new GetReportEntriesQuery(userId, from, to, parsedTypes)).stream()
                .map(ReportResourceAssembler::toResource).toList();
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    @Operation(summary = "Create a progress report from an already generated PDF document")
    public ResponseEntity<ProgressReportResource> createReport(@Valid @RequestBody CreateReportResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var types = resource.types().stream().map(ReportResourceAssembler::toType).toList();
        var report = reportCommandService.handle(new CreateReportCommand(userId, resource.from(), resource.to(),
                types, resource.note() == null ? "" : resource.note(), resource.documentBase64()));
        return ResponseEntity.ok(ReportResourceAssembler.toResource(report));
    }

    @GetMapping("/{reportId}/document")
    @Operation(summary = "Download a previously generated report's PDF document")
    public ResponseEntity<ReportDocumentResource> getDocument(@PathVariable Long reportId) {
        var userId = currentUserProvider.requireCurrentUserId();
        var report = reportQueryService.handle(new GetReportByIdQuery(userId, reportId));
        return ResponseEntity.ok(ReportResourceAssembler.toDocumentResource(report));
    }
}
