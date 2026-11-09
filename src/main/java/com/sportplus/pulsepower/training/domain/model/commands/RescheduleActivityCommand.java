package com.sportplus.pulsepower.training.domain.model.commands;
import java.time.LocalDate;
import java.time.LocalTime;
public record RescheduleActivityCommand(Long userId, Long activityId, LocalDate date, LocalTime startTime) {}
