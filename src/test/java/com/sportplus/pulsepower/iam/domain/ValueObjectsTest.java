package com.sportplus.pulsepower.iam.domain;

import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.PersonalGoal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueObjectsTest {

    @Test
    void displayNameTrimsAndCollapsesWhitespace() {
        assertEquals("Ana Maria Lopez", new DisplayName("  Ana   Maria \t Lopez ").value());
    }

    @Test
    void displayNameRejectsNullAndBlank() {
        assertThrows(IllegalArgumentException.class, () -> new DisplayName(null));
        assertThrows(IllegalArgumentException.class, () -> new DisplayName("   "));
    }

    @Test
    void displayNameAcceptsExactly100CharactersAndRejects101() {
        assertEquals(100, new DisplayName("a".repeat(100)).value().length());
        assertThrows(IllegalArgumentException.class, () -> new DisplayName("a".repeat(101)));
    }

    @Test
    void personalGoalTrimsDescription() {
        assertEquals("Run 5k", new PersonalGoal(null, "  Run 5k ", false).description());
    }

    @Test
    void personalGoalRejectsNullBlankAndTooLongDescription() {
        assertThrows(IllegalArgumentException.class, () -> new PersonalGoal(null, null, false));
        assertThrows(IllegalArgumentException.class, () -> new PersonalGoal(null, " ", false));
        assertEquals(200, new PersonalGoal(null, "g".repeat(200), false).description().length());
        assertThrows(IllegalArgumentException.class, () -> new PersonalGoal(null, "g".repeat(201), false));
    }

    @Test
    void personalGoalWithersReturnModifiedCopies() {
        var goal = new PersonalGoal(null, "Sleep 8h", false);
        var withId = goal.withId(3L);
        var completed = withId.withCompleted(true);
        assertNull(goal.id());
        assertEquals(3L, withId.id());
        assertFalse(withId.completed());
        assertTrue(completed.completed());
        assertEquals("Sleep 8h", completed.description());
    }
}
