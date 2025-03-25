package com.senla.aggregator.repository.favorite;

import com.senla.aggregator.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
}
