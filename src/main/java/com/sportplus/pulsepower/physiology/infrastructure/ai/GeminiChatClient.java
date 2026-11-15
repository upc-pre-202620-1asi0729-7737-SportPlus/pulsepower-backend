package com.sportplus.pulsepower.physiology.infrastructure.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportplus.pulsepower.physiology.infrastructure.ai.dto.GeminiRequest;
import com.sportplus.pulsepower.physiology.infrastructure.ai.dto.GeminiResponse;
import com.sportplus.pulsepower.physiology.infrastructure.ai.dto.GeminiRoutineDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GeminiChatClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiChatClient(
            @Qualifier("geminiRestClient") RestClient restClient,
            @Value("${pulsepower.gemini.api-key}") String apiKey,
            @Value("${pulsepower.gemini.model}") String model) {
        this.restClient = restClient;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String ask(String systemPrompt, String userMessage) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new GeminiUnavailableException("API key not configured");
        }
        var requestBody = new GeminiRequest(
            new GeminiRequest.SystemInstruction(List.of(new GeminiRequest.Part(systemPrompt))),
            List.of(new GeminiRequest.Content("user", List.of(new GeminiRequest.Part(userMessage))))
        );
        try {
            var response = restClient.post()
                .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                .body(requestBody)
                .retrieve()
                .body(GeminiResponse.class);
            if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
                throw new GeminiUnavailableException("Empty response from Gemini");
            }
            var text = response.candidates().get(0).content().parts().get(0).text();
            if (text == null || text.isBlank()) {
                throw new GeminiUnavailableException("Empty text in Gemini response");
            }
            return text;
        } catch (GeminiUnavailableException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new GeminiUnavailableException("Gemini unavailable: " + e.getMessage());
        }
    }

    public GeminiRoutineDto generateRoutine(String systemPrompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new GeminiUnavailableException("API key not configured");
        }
        var requestBody = new GeminiRequest(
            new GeminiRequest.SystemInstruction(List.of(new GeminiRequest.Part(systemPrompt))),
            List.of(new GeminiRequest.Content("user", List.of(new GeminiRequest.Part("Genera la rutina ahora.")))),
            new GeminiRequest.GenerationConfig("application/json")
        );
        try {
            var response = restClient.post()
                .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                .body(requestBody)
                .retrieve()
                .body(GeminiResponse.class);
            if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
                throw new GeminiUnavailableException("Empty response from Gemini");
            }
            var json = response.candidates().get(0).content().parts().get(0).text();
            if (json == null || json.isBlank()) {
                throw new GeminiUnavailableException("Empty JSON from Gemini");
            }
            return objectMapper.readValue(json, GeminiRoutineDto.class);
        } catch (GeminiUnavailableException e) {
            throw e;
        } catch (RuntimeException | java.io.IOException e) {
            throw new GeminiUnavailableException("Routine generation failed: " + e.getMessage());
        }
    }
}
