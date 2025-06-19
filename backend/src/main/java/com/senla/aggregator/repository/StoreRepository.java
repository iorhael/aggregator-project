package com.senla.aggregator.repository;

import com.senla.aggregator.model.Store;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    @EntityGraph(attributePaths = {"retailer"})
    Optional<Store> findByIdAndRetailerOwnerId(UUID storeId, UUID ownerId);

    @EntityGraph(attributePaths = {"retailer"})
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Store> findAllWithRetailerBy(Pageable pageable);

    @EntityGraph(attributePaths = {"retailer"})
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<Store> findAllByRetailerOwnerId(UUID ownerId, Pageable pageable);

    @EntityGraph(attributePaths = {"retailer"})
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<Store> findByRetailerId(UUID retailerId, Pageable pageable);

    Optional<Store> findAllByIdAndRetailerOwnerId(UUID storeId, UUID ownerId);

    int countByRetailerOwnerId(UUID retailerOwnerId);
}
