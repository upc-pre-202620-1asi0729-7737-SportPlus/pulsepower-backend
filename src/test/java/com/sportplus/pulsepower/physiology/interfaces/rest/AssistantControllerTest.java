package com.sportplus.pulsepower.physiology.interfaces.rest;

import com.sportplus.pulsepower.physiology.infrastructure.ai.GeminiChatClient;
import com.sportplus.pulsepower.physiology.infrastructure.ai.GeminiUnavailableException;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRecordsByUserIdQuery;
import com.sportplus.pulsepower.sleep.domain.model.valueobjects.SleepSource;
import com.sportplus.pulsepower.sleep.domain.services.SleepQueryService;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.model.queries.GetSessionsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.services.TrainingQueryService;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetCheckInsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.services.WellnessQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AssistantControllerTest {

    @Mock private GeminiChatClient geminiChatClient;
    @Mock private SleepQueryService sleepQueryService;
    @Mock private TrainingQueryService trainingQueryService;
    @Mock private WellnessQueryService wellnessQueryService;
    @Mock private CurrentUserProvider currentUserProvider;

    private AssistantController controller;

    @BeforeEach
    void setUp() {
        controller = new AssistantController(
            currentUserProvider, sleepQueryService, trainingQueryService, wellnessQueryService, geminiChatClient);
    }

    private SleepRecord sleepRecordForToday() {
        var zone = ZoneId.of("America/Lima");
        var todayMidnight = LocalDate.now(zone).atStartOfDay(zone).toInstant();
        return new SleepRecord(1L, Instant.ofEpochSecond(todayMidnight.getEpochSecond() - 3600 * 8),
            todayMidnight, SleepSource.MANUAL, null, null);
    }

    private TrainingSession trainingSessionForToday() {
        return new TrainingSession(1L, LocalDate.now(ZoneId.of("America/Lima")), "running", 45, 7, null, null);
    }

    private WellnessCheckIn wellnessCheckInForToday() {
        return new WellnessCheckIn(1L, LocalDate.now(ZoneId.of("America/Lima")), 4, 3, false, null);
    }

    @Test
    void successfulGeminiResponse_returnsGeminiSourceWithDateRange() {
        when(currentUserProvider.requireCurrentUserId()).thenReturn(1L);
        when(sleepQueryService.handle(any(GetSleepRecordsByUserIdQuery.class)))
            .thenReturn(List.of(sleepRecordForToday()));
        when(trainingQueryService.handle(any(GetSessionsByUserIdQuery.class)))
            .thenReturn(List.of(trainingSessionForToday()));
        when(wellnessQueryService.handle(any(GetCheckInsByUserIdQuery.class)))
            .thenReturn(List.of(wellnessCheckInForToday()));
        when(geminiChatClient.ask(anyString(), anyString())).thenReturn("Dormiste bien.");

        var answer = controller.ask(new AssistantController.Question("¿Cómo dormí esta semana?"));

        assertThat(answer.source()).isEqualTo("gemini-2.0-flash");
        assertThat(answer.answer()).isEqualTo("Dormiste bien.");
        assertThat(answer.from()).isNotNull();
        assertThat(answer.to()).isNotNull();
    }

    @Test
    void geminiUnavailable_returnsFallbackLocalWithDateRange() {
        when(currentUserProvider.requireCurrentUserId()).thenReturn(1L);
        when(sleepQueryService.handle(any(GetSleepRecordsByUserIdQuery.class)))
            .thenReturn(List.of(sleepRecordForToday()));
        when(trainingQueryService.handle(any(GetSessionsByUserIdQuery.class)))
            .thenReturn(List.of(trainingSessionForToday()));
        when(wellnessQueryService.handle(any(GetCheckInsByUserIdQuery.class)))
            .thenReturn(List.of(wellnessCheckInForToday()));
        when(geminiChatClient.ask(anyString(), anyString()))
            .thenThrow(new GeminiUnavailableException("timeout"));

        var answer = controller.ask(new AssistantController.Question("¿Cómo dormí esta semana?"));

        assertThat(answer.source()).isEqualTo("fallback-local");
        assertThat(answer.from()).isNotNull();
        assertThat(answer.to()).isNotNull();
        // HTTP 200 is guaranteed because no exception is thrown
    }

    @Test
    void outOfScopeGuardrail_returnsPlatformGuideAndNeverCallsGemini() {
        when(currentUserProvider.requireCurrentUserId()).thenReturn(1L);

        var answer = controller.ask(new AssistantController.Question("¿Cuál es la capital de Perú?"));

        assertThat(answer.source()).isEqualTo("platform-guide");
        verifyNoInteractions(geminiChatClient);
    }
}
