package com.senla.aggregator.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class TimeUtil {

    private TimeUtil() {
    }

    public static Instant getStartOfDay(int daysOffset) {
        return LocalDate.now()
                .plusDays(daysOffset)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
    }
}
