package com.sportplus.pulsepower.community.application.internal.commandservices;

import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.domain.model.commands.DeleteSharedProgressCommand;
import com.sportplus.pulsepower.community.domain.model.commands.RevokeProgressShareCommand;
import com.sportplus.pulsepower.community.domain.model.commands.SaveSharedProgressCommand;
import com.sportplus.pulsepower.community.domain.model.commands.ShareProgressCommand;
import com.sportplus.pulsepower.community.domain.repositories.SharedProgressRepository;
import com.sportplus.pulsepower.community.domain.services.CommunityCommandService;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class CommunityCommandServiceImpl implements CommunityCommandService {

    private final SharedProgressRepository sharedProgressRepository;
    private final CurrentUserProvider currentUserProvider;

    public CommunityCommandServiceImpl(SharedProgressRepository sharedProgressRepository,
                                       CurrentUserProvider currentUserProvider) {
        this.sharedProgressRepository = sharedProgressRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public SharedProgress handle(SaveSharedProgressCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Progress note");
        var progress = new SharedProgress(command.userId(), command.title(), command.content(), Instant.now());
        return sharedProgressRepository.save(progress);
    }

    @Override
    public void handle(DeleteSharedProgressCommand command) {
        var progress = findOwned(command.progressId());
        sharedProgressRepository.deleteById(progress.getId());
    }

    @Override
    public SharedProgress handle(ShareProgressCommand command) {
        var progress = findOwned(command.progressId());
        progress.share(command.audience());
        return sharedProgressRepository.save(progress);
    }

    @Override
    public SharedProgress handle(RevokeProgressShareCommand command) {
        var progress = findOwned(command.progressId());
        progress.revoke(command.audience());
        return sharedProgressRepository.save(progress);
    }

    private SharedProgress findOwned(Long progressId) {
        var progress = sharedProgressRepository.findById(progressId)
                .orElseThrow(() -> new ResourceNotFoundException("Progress note", progressId));
        currentUserProvider.ensureOwnership(progress.getUserId(), "Progress note");
        return progress;
    }
}
