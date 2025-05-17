package com.senla.aggregator.model.credential;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google")
public record GoogleCredentials(String clientId,
                                String clientSecret,
                                String refreshToken,
                                String fromEmail) {
}
