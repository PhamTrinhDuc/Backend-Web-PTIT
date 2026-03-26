package com.javaweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Value("${payment.gateway.apiKey}")
    private String apiKey;

    @Value("${payment.gateway.secretKey}")
    private String secretKey;

    @Value("${payment.gateway.baseUrl}")
    private String baseUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
