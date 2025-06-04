package com.senla.aggregator.model.credential;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioCredentials(String serverUrl,
                               String accessKey,
                               String secretKey,
                               String externalServerUrl) {
}
