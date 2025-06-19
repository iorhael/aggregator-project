package com.senla.aggregator.repository;

import com.senla.aggregator.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @EntityGraph(attributePaths = "author")
    Optional<Comment> findWithAuthorByIdAndAuthorId(UUID commentId, UUID authorId);

    @EntityGraph(attributePaths = "author")
    Optional<Comment> findWithAuthorById(UUID id);

    @EntityGraph(attributePaths = "author")
    Page<Comment> findAllWithAuthorByProductId(UUID productId, Pageable pageable);

    @EntityGraph(attributePaths = "author")
    Optional<Comment> findByProductIdAndAuthorId(UUID authorId, UUID productId);

    void deleteByIdAndAuthorId(UUID commentId, UUID authorId);
}
