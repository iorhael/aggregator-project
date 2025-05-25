package com.senla.aggregator.controller;

import com.senla.aggregator.service.priceHistory.PriceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/price_histories")
@RequiredArgsConstructor
@Tag(name = "Price Histories Resource", description = "Product price dynamics by day/month")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @Operation(
            summary = "Get daily price dynamics",
            description = "Retrieve product price changes by day for the last N days"
    )
    @GetMapping("/day")
    public Map<LocalDate, BigDecimal> getDailyPriceDynamics(@RequestParam UUID productId,
                                                            @RequestParam int daysOffset) {
        return priceHistoryService.getDailyPriceDynamics(daysOffset, productId);
    }

    @Operation(
            summary = "Get monthly price dynamics",
            description = "Retrieve product price changes by month for the last N months"
    )
    @GetMapping("/month")
    public Map<YearMonth, BigDecimal> getMonthlyPriceDynamics(@RequestParam UUID productId,
                                                              @RequestParam int monthOffset) {
        return priceHistoryService.getMonthlyPriceDynamics(monthOffset, productId);
    }
}
