package com.senla.aggregator.repository.store;

import com.senla.aggregator.model.Store;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    @EntityGraph(attributePaths = {"retailer"})
    Optional<Store> findByRetailerOwnerId(UUID ownerId);

    @EntityGraph(attributePaths = {"retailer"})
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Store> findAllWithRetailerBy(Pageable pageable);

    @EntityGraph(attributePaths = {"retailer"})
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Store> findAllByRetailerOwnerId(UUID ownerId, Pageable pageable);

    @EntityGraph(attributePaths = {"retailer"})
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Store> findByRetailerId(UUID retailerId, Pageable pageable);

    Optional<Store> findByIdAndRetailerOwnerId(UUID storeId, UUID ownerId);
}
