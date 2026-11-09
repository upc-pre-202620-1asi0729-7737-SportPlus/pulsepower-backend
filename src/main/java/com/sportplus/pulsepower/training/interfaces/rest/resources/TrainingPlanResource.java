package com.sportplus.pulsepower.training.interfaces.rest.resources;

import java.util.List;

public record TrainingPlanResource(
        Long id,
        String name,
        String periodFrom,
        String periodTo,
        List<PlannedActivityResource> activities
) {
}
