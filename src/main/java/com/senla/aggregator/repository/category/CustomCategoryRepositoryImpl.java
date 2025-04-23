package com.senla.aggregator.repository.category;

import com.senla.aggregator.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;

import java.util.UUID;

public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public Category getCategoryTree(UUID parentId) {
        return (Category) entityManager.createNamedQuery("category-tree")
                .setParameter("parentId", parentId)
                .unwrap(NativeQuery.class)
                .setTupleTransformer(new CategoryTreeTransformer())
                .setCacheable(true)
                .getSingleResult();
    }
}
