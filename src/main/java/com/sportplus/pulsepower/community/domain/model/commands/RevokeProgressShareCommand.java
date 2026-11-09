package com.sportplus.pulsepower.community.domain.model.commands;

import com.sportplus.pulsepower.community.domain.model.valueobjects.Audience;

public record RevokeProgressShareCommand(Long userId, Long progressId, Audience audience) {
}
