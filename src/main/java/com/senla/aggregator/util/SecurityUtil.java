package com.senla.aggregator.util;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String getPrincipalName() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
