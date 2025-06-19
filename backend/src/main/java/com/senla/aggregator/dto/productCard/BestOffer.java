package com.senla.aggregator.dto.productCard;

import java.math.BigDecimal;

public interface BestOffer {
    String getProductName();

    String getRetailerName();

    BigDecimal getOurPrice();

    BigDecimal getBestPrice();

    Short getWarranty();

    Short getInstallmentPeriod();

    Short getMaxDeliveryTime();
}
