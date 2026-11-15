package com.sportplus.pulsepower.physiology.infrastructure.ai.dto;

import java.util.List;

public record GeminiRequest(SystemInstruction system_instruction, List<Content> contents) {
    public record SystemInstruction(List<Part> parts) {}
    public record Content(String role, List<Part> parts) {}
    public record Part(String text) {}
}
