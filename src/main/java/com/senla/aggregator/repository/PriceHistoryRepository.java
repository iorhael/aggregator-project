package com.senla.aggregator.repository;

import com.senla.aggregator.dto.priceHistory.PriceDynamics;
import com.senla.aggregator.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {

    @NativeQuery(name = "daily-price-dynamics")
    List<PriceDynamics> getDailyPrices(
            @Param("daysOffset") int daysOffset,
            @Param("productId") UUID productId
    );

    @NativeQuery(name = "monthly-price-dynamics")
    List<PriceDynamics> getMonthlyPrices(
            @Param("monthsOffset") int monthOffset,
            @Param("productId") UUID productId
    );
}
