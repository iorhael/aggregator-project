package com.senla.aggregator.repository.product;

import com.senla.aggregator.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @NonNull
    Page<Product> findAll(Specification<Product> spec, @NonNull Pageable pageable);

    @NativeQuery("""
            WITH RECURSIVE category_tree AS (
                SELECT id FROM categories WHERE id = :categoryId
                UNION ALL
                SELECT c.id FROM categories c
                JOIN category_tree ct ON c.parent_id = ct.id
            )
            SELECT p.*
            FROM products p
            JOIN product_categories pc ON p.id = pc.product_id
            JOIN category_tree ct ON pc.category_id = ct.id
            """)
    List<Product> findAllProductsInCategoryTree(@Param("categoryId") UUID categoryId, Pageable pageable);

    @Modifying
    @NativeQuery("UPDATE products SET verified = true WHERE id IN :ids")
    int verifyProducts(@Param("ids") List<UUID> ids);
}
