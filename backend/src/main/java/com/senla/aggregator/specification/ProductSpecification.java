package com.senla.aggregator.specification;

import com.senla.aggregator.model.Category;
import com.senla.aggregator.model.Category_;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.ProductSummary;
import com.senla.aggregator.model.ProductSummary_;
import com.senla.aggregator.model.Product_;
import com.senla.aggregator.model.Vendor;
import com.senla.aggregator.model.Vendor_;
import com.senla.aggregator.util.CriteriaApiUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> hasMinPriceLessThan(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductSummary> productProductSummaryJoin = CriteriaApiUtil.getOrCreateJoin(
                    root,
                    Product_.summary,
                    JoinType.INNER
            );

            return criteriaBuilder.lessThan(productProductSummaryJoin.get(ProductSummary_.minimalPrice), minPrice);
        };
    }

    public static Specification<Product> hasOffersCountBiggerThan(Integer minOffersCount) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductSummary> productProductSummaryJoin = CriteriaApiUtil.getOrCreateJoin(
                    root,
                    Product_.summary,
                    JoinType.INNER
            );

            return criteriaBuilder.gt(productProductSummaryJoin.get(ProductSummary_.offersCount), minOffersCount);
        };
    }

    public static Specification<Product> hasAverageRatingBiggerThan(BigDecimal averageRating) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductSummary> productProductSummaryJoin = CriteriaApiUtil.getOrCreateJoin(
                    root,
                    Product_.summary,
                    JoinType.INNER
            );

            return criteriaBuilder.gt(productProductSummaryJoin.get(ProductSummary_.averageRating), averageRating);
        };
    }

    public static Specification<Product> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(Product_.name)),
                        "%" + name.toLowerCase() + "%"
                );
    }

    // Fix: blaze-persistence???
    public static Specification<Product> hasCategoryNameIn(List<String> categoryNames) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join(Product_.categories);

            return categoryJoin.get(Category_.name).in(categoryNames);
        };
    }

    public static Specification<Product> hasVendorNameLike(String vendorName) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Vendor> vendorJoin = root.join(Product_.vendor);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(vendorJoin.get(Vendor_.name)),
                    "%" + vendorName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Product> hasVerificationStatus(Boolean verificationStatus) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Product_.verified), verificationStatus);
    }

    public static Specification<Product> hasCharacteristics(Map<String, String> characteristics) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.and(characteristics.entrySet()
                        .stream()
                        .map(entry -> criteriaBuilder.isTrue(
                                criteriaBuilder.function(
                                        "jsonb_path_exists",
                                        Boolean.class,
                                        root.get("characteristics"),
                                        criteriaBuilder.literal("$.** ? (@." + entry.getKey() + " == \"" + entry.getValue() + "\")")
                                )
                        ))
                        .toArray(Predicate[]::new)
                )
        );
    }

    public static Specification<Product> buildSpecification(
            String productName,
            String vendorName,
            Boolean verificationStatus,
            BigDecimal minPrice,
            Integer minOffersCount,
            BigDecimal averageRating,
            List<String> categoryNames,
            Map<String, String> characteristics) {

        Specification<Product> spec = Specification.where(null);
        if (Objects.nonNull(productName)) spec = spec.and(hasNameLike(productName));
        if (Objects.nonNull(vendorName)) spec = spec.and(hasVendorNameLike(vendorName));
        if (Objects.nonNull(verificationStatus)) spec = spec.and(hasVerificationStatus(verificationStatus));
        if (Objects.nonNull(minPrice)) spec = spec.and(hasMinPriceLessThan(minPrice));
        if (Objects.nonNull(minOffersCount)) spec = spec.and(hasOffersCountBiggerThan(minOffersCount));
        if (Objects.nonNull(averageRating)) spec = spec.and(hasAverageRatingBiggerThan(averageRating));
        if (Objects.nonNull(categoryNames) && !categoryNames.isEmpty())
            spec = spec.and(hasCategoryNameIn(categoryNames));
        if (Objects.nonNull(characteristics) && !characteristics.isEmpty())
            spec = spec.and(hasCharacteristics(characteristics));

        return spec;
    }
}
