package com.senla.aggregator.dto.priceHistory;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PriceDynamics {
    LocalDate getDate();
    BigDecimal getAveragePrice();
}
