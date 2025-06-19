package com.senla.aggregator.repository;

import com.senla.aggregator.dto.product.ProductIdName;
import com.senla.aggregator.dto.product.ProductNameDescriptionProjection;
import com.senla.aggregator.dto.product.ProductPreviewProjection;
import com.senla.aggregator.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @NonNull
    @EntityGraph(attributePaths = "summary")
    Page<Product> findAll(Specification<Product> spec, @NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"vendor", "summary"})
    Page<Product> findWithVendorBy(Pageable pageable);

    @NativeQuery("""
                SELECT p.id , p.name
                FROM products p
                WHERE p.name IN :names
                AND (:verifiedOnly = FALSE OR p.verified = TRUE)
            """)
    List<ProductIdName> findByNames(
            @Param("names") List<String> names,
            @Param("verifiedOnly") Boolean verifiedOnly
    );

    @Query("SELECT p FROM Product p WHERE p.verified = false AND p.createdAt BETWEEN :periodStart AND :periodEnd")
    List<Product> findUnverified(@Param("periodStart") Instant periodStart,
                                 @Param("periodEnd") Instant periodEnd
    );

    @NativeQuery(name = "products-name-description-in-category-tree")
    List<ProductNameDescriptionProjection> findAllNameDescriptionInCategoryTree(@Param("categoryId") UUID categoryId,
                                                                                @Param("limit") int limit,
                                                                                @Param("offset") int offset
    );

    @NativeQuery(name = "products-in-category-tree")
    List<ProductPreviewProjection> findAllProductsInCategoryTree(@Param("categoryId") UUID categoryId,
                                                                 @Param("limit") int limit,
                                                                 @Param("offset") int offset
    );

    @NativeQuery(name = "products-in-category-tree-count")
    long countProductsInCategoryTree(@Param("categoryId") UUID categoryId);

    @Modifying
    @Query("UPDATE Product p SET p.verified = true WHERE p.id IN :ids")
    int verifyProducts(@Param("ids") List<UUID> ids);
}
