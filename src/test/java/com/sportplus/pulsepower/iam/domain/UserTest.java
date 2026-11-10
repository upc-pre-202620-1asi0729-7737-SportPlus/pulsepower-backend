package com.sportplus.pulsepower.iam.domain;

import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.AccountStatus;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.Focus;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.PersonalGoal;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static User newUser() {
        return new User(new EmailAddress("user@pulse.com"), "hash", new DisplayName("Test User"), Focus.ATHLETE);
    }

    private static User userWithGoals() {
        return new User(1L, new EmailAddress("user@pulse.com"), "hash", new DisplayName("Test User"), Focus.ATHLETE,
                30, 70.0, 175.0, "Running", "Mon-Fri", "None", AccountStatus.ACTIVE,
                List.of(new PersonalGoal(10L, "Run 5k", false), new PersonalGoal(11L, "Sleep 8h", false)));
    }

    @Test
    void newUserIsActiveWithEmptyProfileAndNoGoals() {
        var user = newUser();
        assertNull(user.getId());
        assertEquals(AccountStatus.ACTIVE, user.getStatus());
        assertEquals("", user.getMainSport());
        assertTrue(user.getGoals().isEmpty());
    }

    @Test
    void constructorRequiresEmailPasswordHashFocusAndStatus() {
        var name = new DisplayName("Test User");
        var email = new EmailAddress("user@pulse.com");
        assertThrows(IllegalArgumentException.class, () -> new User(null, "hash", name, Focus.ATHLETE));
        assertThrows(IllegalArgumentException.class, () -> new User(email, " ", name, Focus.ATHLETE));
        assertThrows(IllegalArgumentException.class, () -> new User(email, null, name, Focus.ATHLETE));
        assertThrows(IllegalArgumentException.class, () -> new User(email, "hash", name, null));
        assertThrows(IllegalArgumentException.class, () -> new User(1L, email, "hash", name, Focus.ATHLETE,
                null, null, null, null, null, null, null, List.of()));
    }

    @Test
    void updateProfileReplacesFieldsAndTrimsText() {
        var user = newUser();
        user.updateProfile(new DisplayName("New Name"), Focus.WELLNESS_SEEKER, 28, 65.5, 170.0,
                "  Yoga ", " Daily ", " None ");
        assertEquals("New Name", user.getDisplayName().value());
        assertEquals(Focus.WELLNESS_SEEKER, user.getFocus());
        assertEquals(28, user.getAge());
        assertEquals("Yoga", user.getMainSport());
        assertEquals("Daily", user.getTrainingRoutine());
        assertEquals("None", user.getPhysicalHistory());
    }

    @Test
    void updateProfileRequiresNameAndFocus() {
        var user = newUser();
        assertThrows(IllegalArgumentException.class,
                () -> user.updateProfile(null, Focus.ATHLETE, null, null, null, null, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> user.updateProfile(new DisplayName("X"), null, null, null, null, null, null, null));
    }

    @Test
    void changePasswordRejectsBlankHash() {
        var user = newUser();
        user.changePassword("new-hash");
        assertEquals("new-hash", user.getPasswordHash());
        assertThrows(IllegalArgumentException.class, () -> user.changePassword(" "));
        assertThrows(IllegalArgumentException.class, () -> user.changePassword(null));
    }

    @Test
    void disableMarksAccountAsDisabled() {
        var user = newUser();
        user.disable();
        assertEquals(AccountStatus.DISABLED, user.getStatus());
    }

    @Test
    void addGoalAppendsAnIncompleteGoal() {
        var user = newUser();
        user.addGoal("Lose 2kg");
        assertEquals(1, user.getGoals().size());
        assertEquals("Lose 2kg", user.getGoals().get(0).description());
        assertFalse(user.getGoals().get(0).completed());
    }

    @Test
    void setGoalCompletedUpdatesOnlyTheMatchingGoal() {
        var user = userWithGoals();
        user.setGoalCompleted(11L, true);
        assertFalse(user.getGoals().get(0).completed());
        assertTrue(user.getGoals().get(1).completed());
    }

    @Test
    void removeGoalDeletesItAndUnknownIdFails() {
        var user = userWithGoals();
        user.removeGoal(10L);
        assertEquals(1, user.getGoals().size());
        assertEquals(11L, user.getGoals().get(0).id());
        assertThrows(ResourceNotFoundException.class, () -> user.removeGoal(99L));
        assertThrows(ResourceNotFoundException.class, () -> user.setGoalCompleted(99L, true));
    }

    @Test
    void goalsListIsAnUnmodifiableSnapshot() {
        var user = userWithGoals();
        assertThrows(UnsupportedOperationException.class,
                () -> user.getGoals().add(new PersonalGoal(null, "x", false)));
    }
}
