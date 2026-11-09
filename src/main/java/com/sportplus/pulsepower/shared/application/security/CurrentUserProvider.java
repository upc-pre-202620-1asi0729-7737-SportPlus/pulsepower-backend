package com.sportplus.pulsepower.shared.application.security;

/**
 * Resolves the authenticated user from the security context and guards ownership of
 * per-user resources. Implemented in the iam context (it owns the User aggregate and
 * the security pipeline); consumed by every other bounded context so that a training
 * session, a sleep record, a wellness check-in, and so on, can only be read or changed
 * by the user it belongs to.
 */
public interface CurrentUserProvider {

    /**
     * @return the identifier of the currently authenticated user.
     * @throws com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException
     *         when called outside an authenticated request (should not normally happen,
     *         since endpoints that use it require authentication).
     */
    Long requireCurrentUserId();

    /**
     * @throws com.sportplus.pulsepower.shared.domain.exceptions.OwnershipViolationException
     *         when the resource's owner is not the authenticated user.
     */
    void ensureOwnership(Long resourceOwnerId, String resourceName);
}
