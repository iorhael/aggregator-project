package com.senla.aggregator.repository.productCard;

import com.senla.aggregator.dto.productCard.ProductCardBatchCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomProductCardRepositoryImpl implements CustomProductCardRepository {
    private static final String UPSERT_SQL = """
            INSERT INTO product_cards
            (id, product_id, retailer_id, description, warranty, installment_period, max_delivery_time)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (product_id, retailer_id) DO
            UPDATE
            SET description = excluded.description, warranty = excluded.warranty, installment_period = excluded.installment_period, max_delivery_time = excluded.max_delivery_time;
            """;

    private static final String UPDATE_SQL = """
                    UPDATE product_cards
                    SET description = ?,
                        warranty = ?,
                        installment_period = ?,
                        max_delivery_time = ?
                    WHERE product_id = ? AND retailer_id = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchUpsert(List<ProductCardBatchCreateDto> cards) {
        jdbcTemplate.batchUpdate(UPSERT_SQL,
                cards,
                100,
                (PreparedStatement ps, ProductCardBatchCreateDto pc) -> {
                    ps.setObject(1, UUID.randomUUID());
                    ps.setObject(2, pc.getProductId());
                    ps.setObject(3, pc.getRetailerId());
                    ps.setString(4, pc.getDescription());
                    ps.setShort(5, pc.getWarranty());
                    ps.setShort(6, pc.getInstallmentPeriod());
                    ps.setShort(7, pc.getMaxDeliveryTime());
                });
    }

    @Override
    public void batchUpdate(List<ProductCardBatchCreateDto> cards) {
        jdbcTemplate.batchUpdate(UPDATE_SQL,
                cards,
                100,
                (PreparedStatement ps, ProductCardBatchCreateDto pc) -> {
                    ps.setString(1, pc.getDescription());
                    ps.setShort(2, pc.getWarranty());
                    ps.setShort(3, pc.getInstallmentPeriod());
                    ps.setShort(4, pc.getMaxDeliveryTime());
                    ps.setObject(5, pc.getProductId());
                    ps.setObject(6, pc.getRetailerId());
                });
    }
}
