package com.sportplus.pulsepower.physiology.domain;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.RecoveryAssessment;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;
import com.sportplus.pulsepower.physiology.domain.services.RecoveryCalculator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecoveryCalculatorTest {

    private static final Instant NOW = Instant.parse("2026-11-07T12:00:00Z");

    /** The first value is the most recent reading, as the calculator expects. */
    private static List<PhysiologicalRecord> hrv(double... values) {
        var records = new ArrayList<PhysiologicalRecord>();
        for (var value : values) {
            records.add(new PhysiologicalRecord(1L, Metric.HRV, value, "ms", NOW));
        }
        return records;
    }

    private static RecoveryAssessment assessmentWithLevel(int level) {
        return new RecoveryAssessment(level, NOW, "test");
    }

    @Test
    void noHrvReadingsMeansNoAssessment() {
        assertTrue(RecoveryCalculator.assess(List.of()).isEmpty());
        var onlyStrain = List.of(new PhysiologicalRecord(1L, Metric.STRAIN, 12, "", NOW));
        assertTrue(RecoveryCalculator.assess(onlyStrain).isEmpty());
    }

    @Test
    void latestEqualToBaselineGivesLevel60() {
        var assessment = RecoveryCalculator.assess(hrv(100, 100, 100)).orElseThrow();
        assertEquals(60, assessment.level());
        assertEquals("Your latest HRV reading is close to your recent baseline.", assessment.explanation());
    }

    @Test
    void latestAboveBaselineRaisesTheLevel() {
        var assessment = RecoveryCalculator.assess(hrv(120, 80, 100)).orElseThrow();
        assertEquals(80, assessment.level());
        assertEquals("Your latest HRV reading is at or above your recent baseline.", assessment.explanation());
    }

    @Test
    void latestBelowBaselineLowersTheLevel() {
        var assessment = RecoveryCalculator.assess(hrv(60, 100, 140)).orElseThrow();
        assertEquals(20, assessment.level());
        assertEquals("Your latest HRV reading is below your recent baseline.", assessment.explanation());
    }

    @Test
    void levelIsClampedBetween0And100() {
        assertEquals(100, RecoveryCalculator.assess(hrv(1000, 10, 10)).orElseThrow().level());
        assertEquals(0, RecoveryCalculator.assess(hrv(0, 100, 100, 100)).orElseThrow().level());
    }

    @Test
    void zeroBaselineDoesNotDivideByZero() {
        assertEquals(60, RecoveryCalculator.assess(hrv(0, 0)).orElseThrow().level());
    }

    @Test
    void nonHrvReadingsAreIgnored() {
        var records = new ArrayList<>(hrv(100, 100));
        records.add(0, new PhysiologicalRecord(1L, Metric.RESTING_HR, 55, "bpm", NOW));
        assertEquals(60, RecoveryCalculator.assess(records).orElseThrow().level());
    }

    @Test
    void noAssessmentMeansNoRecommendations() {
        assertTrue(RecoveryCalculator.recommend(null).isEmpty());
    }

    @Test
    void lowRecoveryRecommendsRest() {
        var recommendation = RecoveryCalculator.recommend(assessmentWithLevel(39));
        assertEquals(1, recommendation.size());
        assertEquals("prioritize-rest", recommendation.get(0).id());
    }

    @Test
    void mediumRecoveryRecommendsModerateLoad() {
        assertEquals("moderate-load", RecoveryCalculator.recommend(assessmentWithLevel(40)).get(0).id());
        assertEquals("moderate-load", RecoveryCalculator.recommend(assessmentWithLevel(69)).get(0).id());
    }

    @Test
    void highRecoveryRecommendsPushing() {
        assertEquals("ready-to-push", RecoveryCalculator.recommend(assessmentWithLevel(70)).get(0).id());
        assertEquals("ready-to-push", RecoveryCalculator.recommend(assessmentWithLevel(100)).get(0).id());
    }
}
