package com.senla.aggregator.repository.category;

import com.senla.aggregator.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByName(String name);

    @NativeQuery("""
            WITH RECURSIVE subcategories AS (
                SELECT id, name, description, parent_id, created_at
                FROM categories
                WHERE name = :parentName
            
                UNION ALL
            
                SELECT c.id, c.name, c.description, c.parent_id, c.created_at
                FROM categories c
                JOIN subcategories s ON c.parent_id = s.id
            )
            SELECT * FROM subcategories
            """)
    List<Category> findChildren(@Param("parentName") String parentName);
}
