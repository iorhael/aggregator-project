package com.senla.aggregator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products_summary")
@Cache(region = "productsSummaryCache", usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
@Getter
public class ProductSummary {

    @Id
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "average_rating")
    private BigDecimal averageRating;

    @Column(name = "comments_count")
    private Integer commentsCount;

    @Column(name = "offers_count")
    private Integer offersCount;

    @Column(name = "minimal_price")
    private BigDecimal minimalPrice;
}
