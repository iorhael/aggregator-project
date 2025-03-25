package com.senla.aggregator.repository.productCard;

import com.senla.aggregator.model.ProductCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCardRepository extends JpaRepository<ProductCard, UUID>, JpaSpecificationExecutor<ProductCard> {

    @EntityGraph(attributePaths = {"retailer", "product", "priceHistories"})
    List<ProductCard> findWithRetailerAndProductByRetailerOwnerId(UUID ownerId, Pageable pageable);

    @EntityGraph(attributePaths = {"retailer", "product", "priceHistories"})
    List<ProductCard> findWithRetailerAndProductByRetailerName(String retailerName, Pageable pageable);

    @EntityGraph(attributePaths = {"retailer", "product", "priceHistories"})
    Page<ProductCard> findAll(Specification<ProductCard> spec, Pageable pageable);

    Optional<ProductCard> findByIdAndRetailerOwnerId(UUID id, UUID ownerId);
}
