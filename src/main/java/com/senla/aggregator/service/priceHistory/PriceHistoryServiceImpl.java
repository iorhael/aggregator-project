package com.senla.aggregator.service.priceHistory;

import com.senla.aggregator.dto.priceHistory.PriceDynamics;
import com.senla.aggregator.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceHistoryServiceImpl implements PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    @Override
    public Map<LocalDate, BigDecimal> getDailyPriceDynamics(int daysOffset, UUID categoryId) {
        return priceHistoryRepository.getDailyPrices(daysOffset, categoryId)
                .stream()
                .collect(Collectors.toMap(
                        PriceDynamics::getDate,
                        PriceDynamics::getAveragePrice,
                        (firstVal, secondVal) -> firstVal, // заглушка, т.к. ключи никогда не будут дублироваться
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<YearMonth, BigDecimal> getMonthlyPriceDynamics(int daysOffset, UUID categoryId) {
        return priceHistoryRepository.getMonthlyPrices(daysOffset, categoryId)
                .stream()
                .collect(Collectors.toMap(
                        k -> YearMonth.from(k.getDate()),
                        PriceDynamics::getAveragePrice,
                        (firstVal, secondVal) -> firstVal,
                        LinkedHashMap::new
                ));
    }
}
