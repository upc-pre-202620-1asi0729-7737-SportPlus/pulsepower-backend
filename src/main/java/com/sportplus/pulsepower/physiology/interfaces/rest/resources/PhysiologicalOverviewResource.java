package com.sportplus.pulsepower.physiology.interfaces.rest.resources;

import java.util.List;

public record PhysiologicalOverviewResource(
        WearableConnectionResource connection,
        List<PhysiologicalRecordResource> records,
        RecoveryAssessmentResource assessment,
        List<RecommendationResource> recommendations,
        List<GuidanceAlertResource> alerts,
        boolean available
) {
}
