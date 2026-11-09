package com.sportplus.pulsepower.physiology.interfaces.rest.resources;

public record PhysiologicalRecordResource(
        Long id,
        String metric,
        double value,
        String unit,
        String recordedAt
) {
}
