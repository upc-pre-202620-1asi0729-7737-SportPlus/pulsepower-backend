package com.sportplus.pulsepower.iam.infrastructure.authorization.sfs.services;

import com.sportplus.pulsepower.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException;
import com.sportplus.pulsepower.shared.domain.exceptions.OwnershipViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextCurrentUserProvider implements CurrentUserProvider {

    @Override
    public Long requireCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl user)) {
            throw new BusinessRuleViolationException("No authenticated user in the current request");
        }
        return user.getId();
    }

    @Override
    public void ensureOwnership(Long resourceOwnerId, String resourceName) {
        if (!requireCurrentUserId().equals(resourceOwnerId)) {
            throw new OwnershipViolationException(resourceName);
        }
    }
}
