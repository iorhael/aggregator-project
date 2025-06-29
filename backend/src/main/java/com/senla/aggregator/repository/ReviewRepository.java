package com.senla.aggregator.repository;

import com.senla.aggregator.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @EntityGraph(attributePaths = {"author", "product"})
    Optional<Review> findWithAuthorAndProductById(UUID id);

    @EntityGraph(attributePaths = {"author", "product"})
    Optional<Review> findWithAuthorAndProductByIdAndAuthorId(UUID id, UUID authorId);

    @EntityGraph(attributePaths = {"author", "product"})
    Page<Review> findAllWithAuthorAndProductByProductId(UUID productId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "product"})
    List<Review> findAllWithAuthorAndProductByAuthorUsername(String authorName, Pageable pageable);

    int countByProductId(UUID productId);

    void deleteByIdAndAuthorId(UUID id, UUID authorId);
}
