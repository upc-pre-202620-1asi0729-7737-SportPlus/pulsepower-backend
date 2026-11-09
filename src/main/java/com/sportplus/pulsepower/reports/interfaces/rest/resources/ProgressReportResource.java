package com.sportplus.pulsepower.reports.interfaces.rest.resources;

import java.util.List;

public record ProgressReportResource(
        Long id,
        String from,
        String to,
        List<String> types,
        String requestedAt,
        String fileName
) {
}
