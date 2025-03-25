package com.senla.aggregator.specification;

import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.ProductCard;
import com.senla.aggregator.model.Retailer;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ProductCardSpecification {

    public static Specification<ProductCard> hasProductName(String productName) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductCard, Product> productJoin = root.join("product");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(productJoin.get("name")),
                    "%" + productName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<ProductCard> hasRetailerName(String retailerName) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductCard, Retailer> retailerJoin = root.join("retailer");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(retailerJoin.get("name")),
                    "%" + retailerName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<ProductCard> hasDiscount(String discount) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("additionalProperties").get("discount"),
                        discount
                );
    }

    public static Specification<ProductCard> hasInstallmentAvailable(Boolean installmentAvailable) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("additionalProperties").get("installmentAvailable"),
                        installmentAvailable
                );
    }

    public static Specification<ProductCard> hasColorOption(String color) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isMember(
                        color,
                        root.get("additionalProperties").get("colorOptions")
                );
    }

    public static Specification<ProductCard> hasRamOption(String ram) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isMember(
                        ram,
                        root.get("additionalProperties").get("ramOptions")
                );
    }

    public static Specification<ProductCard> hasSizeOption(String size) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isMember(
                        size,
                        root.get("additionalProperties").get("sizeOptions")
                );
    }

    public static Specification<ProductCard> hasStrapOption(String strap) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isMember(
                        strap,
                        root.get("additionalProperties").get("strapOptions")
                );
    }

    public static Specification<ProductCard> hasWarranty(String warranty) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("additionalProperties").get("warranty"),
                        warranty
                );
    }

    public static Specification<ProductCard> buildSpecification(
            String productName,
            String retailerName,
            String discount,
            Boolean installmentAvailable,
            String color,
            String ram,
            String size,
            String strap,
            String warranty) {

        Specification<ProductCard> spec = Specification.where(null);

        if (Objects.nonNull(productName)) spec = spec.and(hasProductName(productName));
        if (Objects.nonNull(retailerName)) spec = spec.and(hasRetailerName(retailerName));
        if (Objects.nonNull(discount)) spec = spec.and(hasDiscount(discount));
        if (Objects.nonNull(installmentAvailable)) spec = spec.and(hasInstallmentAvailable(installmentAvailable));
        if (Objects.nonNull(color)) spec = spec.and(hasColorOption(color));
        if (Objects.nonNull(ram)) spec = spec.and(hasRamOption(ram));
        if (Objects.nonNull(size)) spec = spec.and(hasSizeOption(size));
        if (Objects.nonNull(strap)) spec = spec.and(hasStrapOption(strap));
        if (Objects.nonNull(warranty)) spec = spec.and(hasWarranty(warranty));

        return spec;
    }

    private ProductCardSpecification() {}
}
