package com.senla.aggregator.repository;

import com.senla.aggregator.model.Retailer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RetailerRepository extends JpaRepository<Retailer, UUID> {

    Optional<Retailer> findByOwnerId(UUID id);

    List<Retailer> findAllBy(Pageable pageable);

    void deleteByOwnerId(UUID ownerId);
}
