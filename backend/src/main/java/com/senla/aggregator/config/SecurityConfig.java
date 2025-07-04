package com.senla.aggregator.config;

import com.senla.aggregator.security.KeycloakJwtTokenConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE");
    private static final List<String> ALLOWED_HEADERS = List.of("*");
    private static final String CORS_PATTERN = "/**";

    @Value("${server.cors-allowed-origins}")
    private String[] allowedOrigins;

    private static final String[] AUTH_WHITELIST = {
            "/api/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };

    private static final String[] AUTH_GET_WHITELIST = {
            "/api/retailers/*",
            "/api/categories",
            "/api/categories/*",
            "/api/categories/subcategories/*",
            "/api/comments/*",
            "/api/comments/products/*",
            "/api/price_histories/*",
            "/api/product_cards/*",
            "/api/products/*",
            "/api/products/previews",
            "/api/products/previews/*",
            "/api/reviews/*",
            "/api/reviews/count/*",
            "/api/reviews/products/*",
            "/api/reviews/authors/*",
            "/api/stores",
            "/api/stores/*",
            "/api/vendors",
            "/api/vendors/*"
    };

    private static final String[] AUTH_POST_WHITELIST = {
            "/api/product_cards/search/*",
            "/api/products/search"
    };

    private final KeycloakJwtTokenConverter keycloakJwtTokenConverter;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, AUTH_GET_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, AUTH_POST_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(jwtCustomizer -> jwtCustomizer.jwtAuthenticationConverter(keycloakJwtTokenConverter))
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CORS_PATTERN, configuration);
        return source;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        String hierarchy = """
                ROLE_ADMIN > ROLE_RETAILER > ROLE_AUTHOR > ROLE_DEFAULT
                ROLE_RETAILER > ROLE_DEFAULT
                ROLE_AUTHOR > ROLE_DEFAULT
                """;

        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }
}
