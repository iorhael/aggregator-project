package com.senla.aggregator.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class KeycloakJwtTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String RESOURCE_ACCESS = "resource_access";
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Set<GrantedAuthority> authorities = Stream.concat(
                Optional.of(jwtGrantedAuthoritiesConverter.convert(jwt))
                        .orElseGet(Collections::emptySet)
                        .stream(),
                extractClientRoles(jwt).stream()
        ).collect(Collectors.toSet());

        String principalClaimName = jwt.getClaimAsString(JwtClaimNames.SUB);

        return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS);

        if (Objects.isNull(resourceAccess)) return Set.of();
        if (Objects.isNull(resourceAccess.get(keycloakClientId))) return Set.of();

        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(keycloakClientId);
        Collection<String> roles = (Collection<String>) clientAccess.get(ROLES);

        return roles.stream()
                .map(ROLE_PREFIX::concat)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
