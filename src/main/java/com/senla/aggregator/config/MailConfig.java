package com.senla.aggregator.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.senla.aggregator.dto.mail.GoogleTokenRequestDto;
import com.senla.aggregator.model.credential.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

@org.springframework.context.annotation.Configuration
public class MailConfig {
    private static final String REFRESH_TOKEN = "refresh_token";

    @Bean
    public HttpTransport httpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public GoogleTokenRequestDto tokenRequestDto(GoogleCredentials credentials) {
        return new GoogleTokenRequestDto(
                credentials.clientId(),
                credentials.clientSecret(),
                credentials.refreshToken(),
                REFRESH_TOKEN
        );
    }

    @Bean
    public JsonFactory jsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
