package com.senla.aggregator.config;

import com.senla.aggregator.model.credential.KeycloakCredentials;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakCredentials keycloakCredentials;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakCredentials.serverUrl())
                .realm(keycloakCredentials.realm())
                .clientId(keycloakCredentials.clientId())
                .clientSecret(keycloakCredentials.clientSecret())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .username(keycloakCredentials.username())
                .password(keycloakCredentials.password())
                .build();
    }
}
