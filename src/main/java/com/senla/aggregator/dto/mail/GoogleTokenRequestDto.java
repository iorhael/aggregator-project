package com.senla.aggregator.dto.mail;

import jakarta.annotation.Nullable;

public record GoogleTokenRequestDto(@Nullable String clientId,
                                    @Nullable String clientSecret,
                                    @Nullable String refreshToken,
                                    @Nullable String grantType) {
}
