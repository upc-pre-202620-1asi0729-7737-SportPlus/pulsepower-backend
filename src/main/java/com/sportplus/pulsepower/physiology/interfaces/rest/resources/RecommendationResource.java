package com.sportplus.pulsepower.physiology.interfaces.rest.resources;

public record RecommendationResource(
        String id,
        String title,
        String explanation,
        String suggestedAction
) {
}
