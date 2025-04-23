package com.senla.aggregator.repository.category;

import com.senla.aggregator.model.Category;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>, CustomCategoryRepository {

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Category> findTopLevel(Pageable pageable);

    @NativeQuery(name = "child-categories-names")
    List<String> getChildrenNamesBy(@Param("parentName") String parentName);
}
