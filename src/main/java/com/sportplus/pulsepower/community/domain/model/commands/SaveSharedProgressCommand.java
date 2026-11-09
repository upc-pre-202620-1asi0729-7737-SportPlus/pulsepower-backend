package com.sportplus.pulsepower.community.domain.model.commands;

public record SaveSharedProgressCommand(Long userId, String title, String content) {
}
