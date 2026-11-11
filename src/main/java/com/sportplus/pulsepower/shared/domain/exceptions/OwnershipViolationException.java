package com.sportplus.pulsepower.shared.domain.exceptions;

/**
 * Raised when a user tries to read or change a resource that belongs to someone else.
 */
public class OwnershipViolationException extends RuntimeException {

    public OwnershipViolationException(String resourceName) {
        super("You are not allowed to access this %s".formatted(resourceName));
    }
}
