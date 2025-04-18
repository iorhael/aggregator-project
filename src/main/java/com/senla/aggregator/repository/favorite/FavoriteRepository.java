package com.senla.aggregator.repository.favorite;

import com.senla.aggregator.model.Favorite;
import com.senla.aggregator.model.UserTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    Optional<Favorite> findByIdAndUserId(UUID id, UUID userId);

    List<Favorite> findAllByUserId(UUID userId, Pageable pageable);

    List<Favorite> findAllByUserIdAndTag(UUID userId, UserTag tag, Pageable pageable);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
