package com.senla.aggregator.repository;

import com.senla.aggregator.model.Retailer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RetailerRepository extends JpaRepository<Retailer, UUID> {

    @EntityGraph(attributePaths = {"owner"})
    Optional<Retailer> findWithOwnerByOwnerId(UUID id);

    @EntityGraph(attributePaths = {"owner"})
    List<Retailer> findWithOwnerBy(Pageable pageable);

    Optional<Retailer> findRetailerByOwnerId(UUID ownerId);

    void deleteByOwnerId(UUID ownerId);
}
