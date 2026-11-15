package com.sportplus.pulsepower.physiology.infrastructure.ai.dto;

import java.util.List;

public record GeminiRoutineDto(String planName, String summary, List<Activity> activities) {
    public record Activity(int dayOffset, String activity, int durationMinutes,
                           String intensity, String kind, String startTime) {}
}
