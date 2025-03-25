package com.senla.aggregator.repository.store;

import com.senla.aggregator.model.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    @EntityGraph(attributePaths = {"retailer"})
    Optional<Store> findByRetailerOwnerId(UUID ownerId);

    @EntityGraph(attributePaths = {"retailer"})
    List<Store> findWithRetailerBy(Pageable pageable); // сортировка по имени ретейлера

    @EntityGraph(attributePaths = {"retailer"})
    List<Store> findAllByRetailerOwnerId(UUID ownerId, Pageable pageable);

    @EntityGraph(attributePaths = {"retailer"})
    List<Store> findByRetailerName(String retailerName, Pageable pageable);

    Optional<Store> findByIdAndRetailerOwnerId(UUID storeId, UUID ownerId);
}
