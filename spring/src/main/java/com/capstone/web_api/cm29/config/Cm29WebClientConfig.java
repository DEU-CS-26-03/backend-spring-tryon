package com.capstone.web_api.cm29.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Cm29WebClientConfig {

    @Value("${cm29.api.base-url}")
    private String baseUrl;

    @Value("${cm29.api.authorization}")
    private String authorization;

    @Value("${cm29.api.partner-key}")
    private String partnerKey;

    @Bean
    public WebClient cm29WebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + authorization)
                .defaultHeader("Partner-Key", partnerKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}