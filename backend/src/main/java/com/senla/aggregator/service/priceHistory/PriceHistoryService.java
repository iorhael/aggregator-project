package com.senla.aggregator.service.priceHistory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.UUID;

public interface PriceHistoryService {

    Map<LocalDate, BigDecimal> getDailyPriceDynamics(int daysOffset, UUID categoryId);

    Map<YearMonth, BigDecimal> getMonthlyPriceDynamics(int daysOffset, UUID categoryId);
}
