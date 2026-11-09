package com.sportplus.pulsepower.community.interfaces.rest;

import com.sportplus.pulsepower.community.domain.model.commands.DeleteSharedProgressCommand;
import com.sportplus.pulsepower.community.domain.model.commands.RevokeProgressShareCommand;
import com.sportplus.pulsepower.community.domain.model.commands.SaveSharedProgressCommand;
import com.sportplus.pulsepower.community.domain.model.commands.ShareProgressCommand;
import com.sportplus.pulsepower.community.domain.model.queries.GetEarnedAchievementsByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.queries.GetSharedProgressByUserIdQuery;
import com.sportplus.pulsepower.community.domain.model.queries.GetStreakByUserIdQuery;
import com.sportplus.pulsepower.community.domain.services.CommunityCommandService;
import com.sportplus.pulsepower.community.domain.services.CommunityQueryService;
import com.sportplus.pulsepower.community.interfaces.rest.resources.AchievementResource;
import com.sportplus.pulsepower.community.interfaces.rest.resources.SaveSharedProgressResource;
import com.sportplus.pulsepower.community.interfaces.rest.resources.ShareProgressResource;
import com.sportplus.pulsepower.community.interfaces.rest.resources.SharedProgressResource;
import com.sportplus.pulsepower.community.interfaces.rest.resources.StreakResource;
import com.sportplus.pulsepower.community.interfaces.rest.transform.CommunityResourceAssembler;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/community", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Community", description = "Private progress notes, activity streaks and earned achievements")
public class CommunityController {

    private final CommunityQueryService communityQueryService;
    private final CommunityCommandService communityCommandService;
    private final CurrentUserProvider currentUserProvider;

    public CommunityController(CommunityQueryService communityQueryService,
                               CommunityCommandService communityCommandService,
                               CurrentUserProvider currentUserProvider) {
        this.communityQueryService = communityQueryService;
        this.communityCommandService = communityCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/progress")
    @Operation(summary = "List the authenticated user's private progress notes")
    public ResponseEntity<List<SharedProgressResource>> getProgress() {
        var userId = currentUserProvider.requireCurrentUserId();
        var progress = communityQueryService.handle(new GetSharedProgressByUserIdQuery(userId)).stream()
                .map(CommunityResourceAssembler::toResource).toList();
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/progress")
    @Operation(summary = "Save a private progress note")
    public ResponseEntity<SharedProgressResource> saveProgress(@Valid @RequestBody SaveSharedProgressResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var progress = communityCommandService.handle(
                new SaveSharedProgressCommand(userId, resource.title(), resource.content()));
        return ResponseEntity.ok(CommunityResourceAssembler.toResource(progress));
    }

    @DeleteMapping("/progress/{progressId}")
    @Operation(summary = "Delete a private progress note")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long progressId) {
        var userId = currentUserProvider.requireCurrentUserId();
        communityCommandService.handle(new DeleteSharedProgressCommand(userId, progressId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/progress/{progressId}/shares")
    @Operation(summary = "Share a progress note with a group or contact audience")
    public ResponseEntity<SharedProgressResource> share(@PathVariable Long progressId,
                                                        @Valid @RequestBody ShareProgressResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var audience = CommunityResourceAssembler.toAudience(resource.audience());
        var progress = communityCommandService.handle(new ShareProgressCommand(userId, progressId, audience));
        return ResponseEntity.ok(CommunityResourceAssembler.toResource(progress));
    }

    @DeleteMapping("/progress/{progressId}/shares/{audience}")
    @Operation(summary = "Revoke a progress note's share with an audience")
    public ResponseEntity<SharedProgressResource> revoke(@PathVariable Long progressId, @PathVariable String audience) {
        var userId = currentUserProvider.requireCurrentUserId();
        var parsedAudience = CommunityResourceAssembler.toAudience(audience);
        var progress = communityCommandService.handle(new RevokeProgressShareCommand(userId, progressId, parsedAudience));
        return ResponseEntity.ok(CommunityResourceAssembler.toResource(progress));
    }

    @GetMapping("/streak")
    @Operation(summary = "Get the authenticated user's current activity streak")
    public ResponseEntity<StreakResource> getStreak() {
        var userId = currentUserProvider.requireCurrentUserId();
        var streak = communityQueryService.handle(new GetStreakByUserIdQuery(userId));
        return ResponseEntity.ok(CommunityResourceAssembler.toResource(streak));
    }

    @GetMapping("/achievements")
    @Operation(summary = "List the achievements the authenticated user has earned")
    public ResponseEntity<List<AchievementResource>> getAchievements() {
        var userId = currentUserProvider.requireCurrentUserId();
        var earned = communityQueryService.handle(new GetEarnedAchievementsByUserIdQuery(userId)).stream()
                .map(CommunityResourceAssembler::toResource).toList();
        return ResponseEntity.ok(earned);
    }
}
