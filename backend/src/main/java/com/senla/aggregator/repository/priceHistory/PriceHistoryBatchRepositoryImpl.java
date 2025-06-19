package com.senla.aggregator.repository.priceHistory;

import com.senla.aggregator.dto.priceHistory.PriceHistoryBatchCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PriceHistoryBatchRepositoryImpl implements PriceHistoryBatchRepository {
    private static final String INSERT_SQL = "INSERT INTO price_histories (id, card_id, price) VALUES (?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<PriceHistoryBatchCreateDto> prices) {
        jdbcTemplate.batchUpdate(INSERT_SQL,
                prices,
                100,
                (PreparedStatement ps, PriceHistoryBatchCreateDto ph) -> {
                    ps.setObject(1, UUID.randomUUID());
                    ps.setObject(2, ph.getCardId());
                    ps.setBigDecimal(3, ph.getPrice());
                });
    }
}
