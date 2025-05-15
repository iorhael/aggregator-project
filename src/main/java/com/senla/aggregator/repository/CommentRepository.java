package com.senla.aggregator.repository;

import com.senla.aggregator.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @EntityGraph(attributePaths = "author")
    Optional<Comment> findWithAuthorByIdAndAuthorId(UUID commentId, UUID authorId);

    @EntityGraph(attributePaths = "author")
    Optional<Comment> findWithAuthorById(UUID id);

    @EntityGraph(attributePaths = "author")
    List<Comment> findAllWithAuthorByProductId(UUID productId, Pageable pageable);

    void deleteByIdAndAuthorId(UUID commentId, UUID authorId);
}
