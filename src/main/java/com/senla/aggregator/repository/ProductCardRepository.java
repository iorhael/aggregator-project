package com.senla.aggregator.repository;

import com.senla.aggregator.dto.productCard.BestOffer;
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

public interface ProductCardRepository extends JpaRepository<ProductCard, UUID>, JpaSpecificationExecutor<ProductCard> {

    @EntityGraph("product-card-detailed")
    Optional<ProductCard> findDetailedById(UUID id);

    Optional<ProductCard> findByIdAndRetailerOwnerId(UUID id, UUID ownerId);

    @EntityGraph("product-card-detailed")
    Optional<ProductCard> findDetailedByIdAndRetailerOwnerId(UUID id, UUID ownerId);

    @EntityGraph("product-card-detailed")
    List<ProductCard> findAllByRetailerOwnerId(UUID retailerOwnerId, Pageable pageable);

    @Query("""
            SELECT pc
            FROM ProductCard pc
            JOIN pc.product p
            WHERE p.name IN :productNames
              AND pc.retailer.id = :retailerId
            """)
    List<ProductCard> findByProductNamesAndRetailerId(
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
