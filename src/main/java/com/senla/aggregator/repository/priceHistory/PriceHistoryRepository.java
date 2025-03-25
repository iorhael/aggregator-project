package com.senla.aggregator.repository.priceHistory;

import com.senla.aggregator.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {
}
