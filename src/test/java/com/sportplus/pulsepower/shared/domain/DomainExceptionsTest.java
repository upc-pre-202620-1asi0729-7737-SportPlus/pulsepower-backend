package com.sportplus.pulsepower.shared.domain;

import com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException;
import com.sportplus.pulsepower.shared.domain.exceptions.OwnershipViolationException;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainExceptionsTest {

    @Test
    void resourceNotFoundMessageIncludesNameAndIdentifier() {
        var exception = new ResourceNotFoundException("Personal goal", 7L);
        assertEquals("Personal goal with identifier 7 was not found", exception.getMessage());
    }

    @Test
    void resourceAlreadyExistsMessageIncludesFieldAndValue() {
        var exception = new ResourceAlreadyExistsException("User", "email", "a@b.com");
        assertEquals("User with email a@b.com already exists", exception.getMessage());
    }

    @Test
    void ownershipViolationMessageNamesTheResource() {
        var exception = new OwnershipViolationException("training session");
        assertEquals("You are not allowed to access this training session", exception.getMessage());
    }

    @Test
    void businessRuleViolationKeepsItsMessage() {
        assertEquals("rule", new BusinessRuleViolationException("rule").getMessage());
    }
}
