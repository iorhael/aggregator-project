package com.senla.aggregator.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String SCHEME = "bearer";
    private static final String SCHEME_NAME = "bearerAuth";
    private static final String BEARER_FORMAT = "JWT";

    @Value("${swagger.application.title}")
    private String title;

    @Value("${spring.application.version}")
    private String version;

    @Value("${swagger.application.description}")
    private String description;

    @Bean
    public OpenAPI caseOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(SCHEME_NAME)).components(new Components()
                        .addSecuritySchemes(
                                SCHEME_NAME, new SecurityScheme()
                                        .name(SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat(BEARER_FORMAT)
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme(SCHEME)
                        )
                )
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                );
    }
}
