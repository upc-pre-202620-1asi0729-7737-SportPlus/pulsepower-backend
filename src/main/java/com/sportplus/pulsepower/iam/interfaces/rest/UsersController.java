package com.sportplus.pulsepower.iam.interfaces.rest;

import com.sportplus.pulsepower.iam.domain.model.commands.*;
import com.sportplus.pulsepower.iam.domain.model.queries.GetUserByIdQuery;
import com.sportplus.pulsepower.iam.domain.services.UserCommandService;
import com.sportplus.pulsepower.iam.domain.services.UserQueryService;
import com.sportplus.pulsepower.iam.interfaces.rest.resources.*;
import com.sportplus.pulsepower.iam.interfaces.rest.transform.ProfileCommandAssembler;
import com.sportplus.pulsepower.iam.interfaces.rest.transform.UserResourceAssembler;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "The authenticated user's account, profile and personal goals")
public class UsersController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final CurrentUserProvider currentUserProvider;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService,
                           CurrentUserProvider currentUserProvider) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/me")
    @Operation(summary = "Get the authenticated user's account")
    public ResponseEntity<UserResource> getCurrentUser() {
        var userId = currentUserProvider.requireCurrentUserId();
        return userQueryService.handle(new GetUserByIdQuery(userId))
                .map(user -> ResponseEntity.ok(UserResourceAssembler.toResource(user)))
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    @PutMapping("/me/profile")
    @Operation(summary = "Update the authenticated user's profile")
    public ResponseEntity<UserResource> updateProfile(@Valid @RequestBody UpdateProfileResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var user = userCommandService.handle(ProfileCommandAssembler.toCommand(userId, resource));
        return ResponseEntity.ok(UserResourceAssembler.toResource(user));
    }

    @PutMapping("/me/password")
    @Operation(summary = "Change the authenticated user's password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        userCommandService.handle(new ChangePasswordCommand(userId, resource.currentPassword(), resource.newPassword()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    @Operation(summary = "Disable the authenticated user's account")
    public ResponseEntity<Void> disableAccount() {
        var userId = currentUserProvider.requireCurrentUserId();
        userCommandService.handle(new DisableAccountCommand(userId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/goals")
    @Operation(summary = "Add a personal goal")
    public ResponseEntity<UserResource> addGoal(@Valid @RequestBody SaveGoalResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var user = userCommandService.handle(new AddPersonalGoalCommand(userId, resource.description()));
        return ResponseEntity.ok(UserResourceAssembler.toResource(user));
    }

    @PutMapping("/me/goals/{goalId}")
    @Operation(summary = "Mark a personal goal as completed or pending")
    public ResponseEntity<UserResource> updateGoal(@PathVariable Long goalId,
                                                   @Valid @RequestBody UpdateGoalResource resource) {
        var userId = currentUserProvider.requireCurrentUserId();
        var user = userCommandService.handle(new UpdatePersonalGoalCommand(userId, goalId, resource.completed()));
        return ResponseEntity.ok(UserResourceAssembler.toResource(user));
    }

    @DeleteMapping("/me/goals/{goalId}")
    @Operation(summary = "Remove a personal goal")
    public ResponseEntity<UserResource> removeGoal(@PathVariable Long goalId) {
        var userId = currentUserProvider.requireCurrentUserId();
        var user = userCommandService.handle(new RemovePersonalGoalCommand(userId, goalId));
        return ResponseEntity.ok(UserResourceAssembler.toResource(user));
    }
}
