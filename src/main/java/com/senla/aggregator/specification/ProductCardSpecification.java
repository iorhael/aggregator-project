package com.senla.aggregator.specification;

import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.ProductCard;
import com.senla.aggregator.model.ProductCard_;
import com.senla.aggregator.model.Product_;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.model.Retailer_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.UUID;

public class ProductCardSpecification {

    private ProductCardSpecification() {
    }

    public static Specification<ProductCard> hasProductId(UUID productId) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductCard, Product> productJoin = root.join(ProductCard_.product);

            return criteriaBuilder.equal(productJoin.get(Product_.id), productId);
        };
    }

    public static Specification<ProductCard> hasRetailerName(String retailerName) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductCard, Retailer> retailerJoin = root.join(ProductCard_.retailer);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(retailerJoin.get(Retailer_.name)),
                    "%" + retailerName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<ProductCard> hasWarranty(Short warranty) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(ProductCard_.warranty), warranty);
    }

    public static Specification<ProductCard> hasInstallmentPeriod(Short installmentPeriod) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(ProductCard_.installmentPeriod), installmentPeriod);
    }

    public static Specification<ProductCard> hasMaxDeliveryTime(Short maxDeliveryTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(ProductCard_.maxDeliveryTime), maxDeliveryTime);
    }

    public static Specification<ProductCard> buildSpecification(
            UUID productId,
            String retailerName,
            Short warranty,
            Short installmentPeriod,
            Short maxDeliveryTime) {

        Specification<ProductCard> spec = Specification.where(null);
        if (Objects.nonNull(productId)) spec = spec.and(hasProductId(productId));
        if (Objects.nonNull(retailerName)) spec = spec.and(hasRetailerName(retailerName));
        if (Objects.nonNull(warranty)) spec = spec.and(hasWarranty(warranty));
        if (Objects.nonNull(installmentPeriod)) spec = spec.and(hasInstallmentPeriod(installmentPeriod));
        if (Objects.nonNull(maxDeliveryTime)) spec = spec.and(hasMaxDeliveryTime(maxDeliveryTime));

        return spec;
    }
}
