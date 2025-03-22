package com.senla.aggregator.util;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    public static String getPrincipalName() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private SecurityUtil() {}
}
