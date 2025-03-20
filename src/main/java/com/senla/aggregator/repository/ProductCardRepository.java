package com.senla.aggregator.repository;

import com.senla.aggregator.model.ProductCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCardRepository extends JpaRepository<ProductCard, UUID> {
}
