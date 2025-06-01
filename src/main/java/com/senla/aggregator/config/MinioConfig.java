package com.senla.aggregator.config;

import com.senla.aggregator.model.credential.MinioCredentials;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioCredentials minioCredentials) {
        return MinioClient.builder()
                .endpoint(minioCredentials.serverUrl())
                .credentials(minioCredentials.accessKey(), minioCredentials.secretKey())
                .build();
    }
}
