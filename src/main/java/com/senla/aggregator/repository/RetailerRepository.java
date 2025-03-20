package com.senla.aggregator.repository;

import com.senla.aggregator.model.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RetailerRepository extends JpaRepository<Retailer, UUID> {
}
