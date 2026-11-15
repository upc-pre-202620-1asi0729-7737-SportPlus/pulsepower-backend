package com.sportplus.pulsepower.physiology.infrastructure.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeminiRequest(SystemInstruction system_instruction, List<Content> contents, GenerationConfig generationConfig) {
    public GeminiRequest(SystemInstruction system_instruction, List<Content> contents) {
        this(system_instruction, contents, null);
    }
    public record GenerationConfig(String responseMimeType) {}
    public record SystemInstruction(List<Part> parts) {}
    public record Content(String role, List<Part> parts) {}
    public record Part(String text) {}
}
