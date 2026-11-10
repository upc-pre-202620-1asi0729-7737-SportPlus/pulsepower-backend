package com.sportplus.pulsepower.iam.domain;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailAddressTest {

    @Test
    void normalizesToTrimmedLowerCase() {
        assertEquals("pulse@power.com", new EmailAddress("  Pulse@Power.COM ").address());
    }

    @Test
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "plainaddress", "a@b", "@power.com", "a b@power.com", "a@@power.com"})
    void rejectsBlankOrMalformedAddresses(String value) {
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress(value));
    }

    @Test
    void equalAddressesAfterNormalizationAreEqual() {
        assertEquals(new EmailAddress("A@b.com"), new EmailAddress(" a@b.com"));
    }
}
