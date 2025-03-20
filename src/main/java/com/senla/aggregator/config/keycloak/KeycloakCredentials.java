package com.senla.aggregator.config.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakCredentials (String serverUrl,
                                   String realm,
                                   String clientDatabaseId,
                                   String clientId,
                                   String clientSecret,
                                   String username,
                                   String password) {
}
