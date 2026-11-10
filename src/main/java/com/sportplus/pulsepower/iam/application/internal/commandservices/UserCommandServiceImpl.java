package com.sportplus.pulsepower.iam.application.internal.commandservices;

import com.sportplus.pulsepower.iam.application.outboundservices.hashing.HashingService;
import com.sportplus.pulsepower.iam.application.outboundservices.tokens.TokenService;
import com.sportplus.pulsepower.iam.domain.exceptions.InvalidCredentialsException;
import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.commands.*;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.AuthenticatedUser;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import com.sportplus.pulsepower.iam.domain.services.UserCommandService;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final CurrentUserProvider currentUserProvider;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService,
                                  TokenService tokenService, CurrentUserProvider currentUserProvider) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public User handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ResourceAlreadyExistsException("User", "email", command.email().address());
        }
        var user = new User(command.email(), hashingService.encode(command.password()),
                command.displayName(), command.focus());
        if (command.initialGoal() != null && !command.initialGoal().isBlank()) {
            user.addGoal(command.initialGoal());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUser handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email())
                .filter(candidate -> hashingService.matches(command.password(), candidate.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);
        return new AuthenticatedUser(user, tokenService.generateToken(user.getEmail().address()));
    }

    @Override
    public User handle(UpdateProfileCommand command) {
        var user = findOwnedUser(command.userId());
        user.updateProfile(command.displayName(), command.focus(), command.age(), command.weightKg(),
                command.heightCm(), command.mainSport(), command.trainingRoutine(), command.physicalHistory());
        return userRepository.save(user);
    }

    @Override
    public User handle(ChangePasswordCommand command) {
        var user = findOwnedUser(command.userId());
        if (!hashingService.matches(command.currentPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        user.changePassword(hashingService.encode(command.newPassword()));
        return userRepository.save(user);
    }

    @Override
    public User handle(DisableAccountCommand command) {
        var user = findOwnedUser(command.userId());
        user.disable();
        return userRepository.save(user);
    }

    @Override
    public User handle(AddPersonalGoalCommand command) {
        var user = findOwnedUser(command.userId());
        user.addGoal(command.description());
        return userRepository.save(user);
    }

    @Override
    public User handle(UpdatePersonalGoalCommand command) {
        var user = findOwnedUser(command.userId());
        user.setGoalCompleted(command.goalId(), command.completed());
        return userRepository.save(user);
    }

    @Override
    public User handle(RemovePersonalGoalCommand command) {
        var user = findOwnedUser(command.userId());
        user.removeGoal(command.goalId());
        return userRepository.save(user);
    }

    private User findOwnedUser(Long userId) {
        currentUserProvider.ensureOwnership(userId, "User");
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
