package com.senla.aggregator.repository;

import com.senla.aggregator.model.AutoUpdateCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutoUpdateCardRepository extends JpaRepository<AutoUpdateCard, UUID> {

    @EntityGraph(attributePaths = "retailer")
    List<AutoUpdateCard> findWithRetailerBy();

    Optional<AutoUpdateCard> findByRetailerOwnerId(UUID retailerOwnerId);

    void deleteByRetailerId(UUID retailerId);
}
