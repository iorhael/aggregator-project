package com.senla.aggregator.service.mail;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.senla.aggregator.dto.GoogleTokenRequestDto;
import com.senla.aggregator.dto.GoogleTokenResponse;
import com.senla.aggregator.service.exception.GmailException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static com.senla.aggregator.service.exception.ExceptionMessages.*;

@Component
@RequiredArgsConstructor
public class GoogleCredentialManager {
    private static final long SAFETY_MARGIN_SECONDS = 60;

    private final RestTemplate restTemplate;
    private final GoogleTokenRequestDto tokenRequestDto;

    @Value("${google.refresh-access-url}")
    private String refreshAccessUrl;

    private Credential credential;
    private Instant expiryTime = Instant.EPOCH;

    @PostConstruct
    public void authorize() { refreshCredential(); }

    public synchronized Credential getCredential() {
        if (notExpired()) return credential;

        synchronized (this) {
            if (notExpired()) return credential;

            refreshCredential();
            return credential;
        }
    }

    private boolean notExpired() {
        return !Instant.now().plusSeconds(SAFETY_MARGIN_SECONDS).isAfter(expiryTime);
    }

    private void refreshCredential() {
        HttpEntity<GoogleTokenRequestDto> entity = new HttpEntity<>(tokenRequestDto);
        GoogleTokenResponse tokenResponse = restTemplate.postForObject(
                refreshAccessUrl,
                entity,
                GoogleTokenResponse.class
        );
        if (Objects.isNull(tokenResponse)) throw new GmailException(ACCESS_TOKEN_NOT_UPDATED);

        credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setFromTokenResponse(tokenResponse);
        long expiresInSeconds = Optional.ofNullable(tokenResponse.getExpiresInSeconds())
                .orElse(3600L);
        expiryTime = Instant.now().plusSeconds(expiresInSeconds);
    }
}
