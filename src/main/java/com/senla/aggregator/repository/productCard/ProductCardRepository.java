package com.senla.aggregator.repository.productCard;

import com.senla.aggregator.dto.productCard.BestOffer;
import com.senla.aggregator.dto.productCard.ProductCardBatchCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardBatchUpdateDto;
import com.senla.aggregator.dto.productCard.ProductCardIdProductName;
import com.senla.aggregator.model.ProductCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCardRepository extends JpaRepository<ProductCard, UUID>,
        CustomProductCardRepository, JpaSpecificationExecutor<ProductCard> {

    @EntityGraph("product-card-detailed")
    Optional<ProductCard> findDetailedById(UUID id);

    Optional<ProductCard> findByIdAndRetailerOwnerId(UUID id, UUID ownerId);

    @EntityGraph("product-card-detailed")
    Optional<ProductCard> findDetailedByIdAndRetailerOwnerId(UUID id, UUID ownerId);

    @EntityGraph("product-card-detailed")
    List<ProductCard> findAllByRetailerOwnerId(UUID retailerOwnerId, Pageable pageable);

    @NativeQuery("""
        SELECT pc.id,
            pc.product_id AS productId,
            p.name AS productName,
            pc.retailer_id AS retailerId,
            pc.description AS description,
            pc.warranty AS warranty,
            pc.installment_period AS installmentPeriod,
            pc.max_delivery_time AS maxDeliveryTime
        FROM product_cards pc
        JOIN products p ON p.id = pc.product_id
        WHERE p.name IN (:productNames)
          AND pc.retailer_id = :retailerId
        """)
    List<ProductCardBatchUpdateDto> findCardForBatchUpdate(
            @Param("productNames") List<String> productNames,
            @Param("retailerId") UUID retailerId
    );

    @NativeQuery("""
            SELECT pc.id, p.name AS productName
            FROM product_cards pc
            JOIN products p ON p.id = pc.product_id
            WHERE p.name IN :productNames
                AND pc.retailer_id = :retailerId
            """)
    List<ProductCardIdProductName> findIdProductNames(
            @Param("productNames") List<String> productNames,
            @Param("retailerId") UUID retailerId
    );

    @NonNull
    @EntityGraph(attributePaths = "latestPrice")
    Page<ProductCard> findAll(Specification<ProductCard> spec, @NonNull Pageable pageable);

    @NativeQuery(name = "best-offers-report")
    List<BestOffer> findBestOffers(@Param("retailerId") UUID retailerId);

    int deleteAllByRetailerOwnerId(UUID retailerOwnerId);
}
