package com.senla.aggregator.repository.product;

import com.senla.aggregator.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @EntityGraph(attributePaths = {"categories"})
    Optional<Product> findWithCategoriesById(UUID id);

    @EntityGraph(attributePaths = {"categories"})
    List<Product> findWithCategoriesBy(Pageable pageable);
}
