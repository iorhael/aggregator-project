package com.senla.aggregator.repository.productCard;

import com.senla.aggregator.model.ProductCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

    @NonNull
    @EntityGraph(value = "product-card-brief")
    Page<ProductCard> findAll(Specification<ProductCard> spec, @NonNull Pageable pageable);
}
