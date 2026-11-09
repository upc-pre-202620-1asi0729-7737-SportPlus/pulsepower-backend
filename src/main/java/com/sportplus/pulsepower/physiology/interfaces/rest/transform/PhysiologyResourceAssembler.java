package com.sportplus.pulsepower.physiology.interfaces.rest.transform;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalOverview;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.RecoveryAssessment;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Recommendation;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.GuidanceAlertResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.PhysiologicalOverviewResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.PhysiologicalRecordResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.RecommendationResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.RecoveryAssessmentResource;
import com.sportplus.pulsepower.physiology.interfaces.rest.resources.WearableConnectionResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toEnum;
import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toValue;

public final class PhysiologyResourceAssembler {

    private PhysiologyResourceAssembler() {
    }

    public static WearableConnectionResource toResource(WearableConnection connection) {
        if (connection == null) return null;
        return new WearableConnectionResource(connection.getId(), connection.getProvider(),
                toValue(connection.getStatus()),
                connection.getLastSyncedAt() == null ? null : connection.getLastSyncedAt().toString());
    }

    public static PhysiologicalRecordResource toResource(PhysiologicalRecord record) {
        return new PhysiologicalRecordResource(record.getId(), toValue(record.getMetric()), record.getValue(),
                record.getUnit(), record.getRecordedAt().toString());
    }

    public static RecoveryAssessmentResource toResource(RecoveryAssessment assessment) {
        if (assessment == null) return null;
        return new RecoveryAssessmentResource(assessment.level(), assessment.calculatedAt().toString(),
                assessment.explanation());
    }

    public static RecommendationResource toResource(Recommendation recommendation) {
        return new RecommendationResource(recommendation.id(), recommendation.title(), recommendation.explanation(),
                recommendation.suggestedAction());
    }

    public static GuidanceAlertResource toResource(GuidanceAlert alert) {
        return new GuidanceAlertResource(alert.getId(), alert.getTitle(), alert.getMessage(),
                alert.getIssuedAt().toString(), alert.getReviewedAt() == null ? null : alert.getReviewedAt().toString());
    }

    public static PhysiologicalOverviewResource toResource(PhysiologicalOverview overview) {
        return new PhysiologicalOverviewResource(
                toResource(overview.connection()),
                overview.records().stream().map(PhysiologyResourceAssembler::toResource).toList(),
                toResource(overview.assessment()),
                overview.recommendations().stream().map(PhysiologyResourceAssembler::toResource).toList(),
                overview.alerts().stream().map(PhysiologyResourceAssembler::toResource).toList(),
                overview.available());
    }

    public static Metric toMetric(String value) {
        return toEnum(Metric.class, value);
    }
}
