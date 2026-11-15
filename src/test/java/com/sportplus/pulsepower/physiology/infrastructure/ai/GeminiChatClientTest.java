package com.sportplus.pulsepower.physiology.infrastructure.ai;

import com.sportplus.pulsepower.physiology.infrastructure.ai.dto.GeminiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class GeminiChatClientTest {

    private MockRestServiceServer mockServer;
    private GeminiChatClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://generativelanguage.googleapis.com");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.build();
        client = new GeminiChatClient(restClient, "test-api-key", "gemini-2.0-flash");
    }

    @Test
    void happyPath_returnsExtractedText() throws Exception {
        var response = new GeminiResponse(java.util.List.of(
            new GeminiResponse.Candidate(new GeminiResponse.Content(
                java.util.List.of(new GeminiResponse.Part("Dormiste bien esta semana."))))));
        mockServer.expect(requestTo(org.hamcrest.Matchers.containsString("generateContent")))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andRespond(withSuccess(mapper.writeValueAsString(response), MediaType.APPLICATION_JSON));

        String result = client.ask("system prompt", "¿Cómo dormí?");
        assertThat(result).isEqualTo("Dormiste bien esta semana.");
        mockServer.verify();
    }

    @Test
    void nonTwoXxResponse_throwsGeminiUnavailableException() {
        mockServer.expect(requestTo(org.hamcrest.Matchers.containsString("generateContent")))
            .andRespond(withServerError());

        assertThatThrownBy(() -> client.ask("system", "question"))
            .isInstanceOf(GeminiUnavailableException.class);
    }

    @Test
    void emptyCandidates_throwsGeminiUnavailableException() throws Exception {
        var response = new GeminiResponse(java.util.List.of());
        mockServer.expect(requestTo(org.hamcrest.Matchers.containsString("generateContent")))
            .andRespond(withSuccess(mapper.writeValueAsString(response), MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> client.ask("system", "question"))
            .isInstanceOf(GeminiUnavailableException.class);
    }

    @Test
    void emptyApiKey_throwsWithoutHttpCall() {
        RestClient.Builder b = RestClient.builder().baseUrl("https://generativelanguage.googleapis.com");
        GeminiChatClient noKeyClient = new GeminiChatClient(b.build(), "", "gemini-2.0-flash");
        assertThatThrownBy(() -> noKeyClient.ask("system", "question"))
            .isInstanceOf(GeminiUnavailableException.class)
            .hasMessageContaining("API key");
    }
}
