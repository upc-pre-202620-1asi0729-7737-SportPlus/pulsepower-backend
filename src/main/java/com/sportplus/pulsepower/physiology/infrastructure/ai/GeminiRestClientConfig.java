package com.sportplus.pulsepower.physiology.infrastructure.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class GeminiRestClientConfig {

    @Bean("geminiRestClient")
    public RestClient geminiRestClient(
            @Value("${pulsepower.gemini.base-url}") String baseUrl,
            @Value("${pulsepower.gemini.timeout.connect-ms}") int connectMs,
            @Value("${pulsepower.gemini.timeout.read-ms}") int readMs) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectMs));
        factory.setReadTimeout(Duration.ofMillis(readMs));
        return RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }
}
