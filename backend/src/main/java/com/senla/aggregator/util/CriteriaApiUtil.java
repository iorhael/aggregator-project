package com.senla.aggregator.util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;

public final class CriteriaApiUtil {

    private CriteriaApiUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <X, Y> Join<X, Y> getOrCreateJoin(Root<X> criteriaRoot,
                                                    SingularAttribute<? super X, Y> attribute,
                                                    JoinType joinType) {
        return (Join<X, Y>) criteriaRoot.getJoins()
                .stream()
                .filter(j -> j.getAttribute().getName().equals(attribute.getName()) && j.getJoinType().equals(joinType))
                .findFirst()
                .orElseGet(() -> criteriaRoot.join(attribute, joinType));
    }
}
