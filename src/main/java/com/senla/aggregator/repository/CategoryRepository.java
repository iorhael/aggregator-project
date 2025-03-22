package com.senla.aggregator.repository;

import com.senla.aggregator.dto.category.CategoryGetDto;
import com.senla.aggregator.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByName(String name);
}
