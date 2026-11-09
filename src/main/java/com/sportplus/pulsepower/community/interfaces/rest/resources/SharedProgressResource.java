package com.sportplus.pulsepower.community.interfaces.rest.resources;

import java.util.List;

public record SharedProgressResource(
        Long id,
        String title,
        String content,
        String visibility,
        String createdAt,
        List<String> audiences
) {
}
