package com.sportplus.pulsepower.community.domain;

import com.sportplus.pulsepower.community.domain.model.aggregates.Achievement;
import com.sportplus.pulsepower.community.domain.model.aggregates.SharedProgress;
import com.sportplus.pulsepower.community.domain.model.aggregates.UserAchievement;
import com.sportplus.pulsepower.community.domain.model.valueobjects.Audience;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommunityAggregatesTest {

    private static final Instant NOW = Instant.parse("2026-11-07T12:00:00Z");

    private static SharedProgress note() {
        return new SharedProgress(1L, "  Week 1 ", "  Good progress ", NOW);
    }

    @Test
    void newNoteIsPrivateAndTrimmed() {
        var note = note();
        assertEquals("Week 1", note.getTitle());
        assertEquals("Good progress", note.getContent());
        assertTrue(note.getAudiences().isEmpty());
    }

    @Test
    void shareAndRevokeAudiences() {
        var note = note();
        note.share(Audience.GROUP);
        note.share(Audience.CONTACT);
        note.share(Audience.GROUP);
        assertEquals(EnumSet.of(Audience.GROUP, Audience.CONTACT), note.getAudiences());
        note.revoke(Audience.GROUP);
        assertEquals(EnumSet.of(Audience.CONTACT), note.getAudiences());
    }

    @Test
    void returnedAudiencesAreACopy() {
        var note = note();
        note.getAudiences().add(Audience.GROUP);
        assertTrue(note.getAudiences().isEmpty());
    }

    @Test
    void titleAndContentLimitsAreEnforced() {
        assertThrows(IllegalArgumentException.class, () -> new SharedProgress(null, "T", "C", NOW));
        assertThrows(IllegalArgumentException.class, () -> new SharedProgress(1L, " ", "C", NOW));
        assertThrows(IllegalArgumentException.class, () -> new SharedProgress(1L, "t".repeat(101), "C", NOW));
        assertEquals(100, new SharedProgress(1L, "t".repeat(100), "C", NOW).getTitle().length());
        assertThrows(IllegalArgumentException.class, () -> new SharedProgress(1L, "T", " ", NOW));
        assertThrows(IllegalArgumentException.class, () -> new SharedProgress(1L, "T", "c".repeat(2001), NOW));
        assertEquals(2000, new SharedProgress(1L, "T", "c".repeat(2000), NOW).getContent().length());
    }

    @Test
    void achievementRequiresCodeAndName() {
        var achievement = new Achievement("  SEVEN_DAYS ", " Seven days ", null);
        assertEquals("SEVEN_DAYS", achievement.getCode());
        assertEquals("Seven days", achievement.getName());
        assertEquals("", achievement.getDescription());
        assertThrows(IllegalArgumentException.class, () -> new Achievement(" ", "Name", "d"));
        assertThrows(IllegalArgumentException.class, () -> new Achievement("CODE", null, "d"));
    }

    @Test
    void userAchievementRequiresOwnerAchievementAndTime() {
        assertEquals(NOW, new UserAchievement(1L, 2L, NOW).getEarnedAt());
        assertThrows(IllegalArgumentException.class, () -> new UserAchievement(null, 2L, NOW));
        assertThrows(IllegalArgumentException.class, () -> new UserAchievement(1L, null, NOW));
        assertThrows(IllegalArgumentException.class, () -> new UserAchievement(1L, 2L, null));
    }
}
