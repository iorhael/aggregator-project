package com.senla.aggregator.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_cards")
@NamedEntityGraph(
        name = "product-card-detailed",
        attributeNodes = {
                @NamedAttributeNode(value = "retailer"),
                @NamedAttributeNode(value = "latestPrice"),
                @NamedAttributeNode(value = "product", subgraph = "product-with-vendor")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "product-with-vendor",
                        attributeNodes = @NamedAttributeNode(value = "vendor")
                )
        }
)
@NamedEntityGraph(
        name = "product-card-brief",
        attributeNodes = {
                @NamedAttributeNode(value = "retailer"),
                @NamedAttributeNode(value = "latestPrice")
        }
)
@Getter
@Setter
public class ProductCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "warranty")
    private Short warranty;

    @Column(name = "installment_period")
    private Short installmentPeriod;

    @Column(name = "max_delivery_time")
    private Short maxDeliveryTime;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retailer_id")
    private Retailer retailer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "card", orphanRemoval = true)
    private List<PriceHistory> priceHistories = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinFormula("""
            (SELECT ph.id
            FROM price_histories ph
            WHERE ph.card_id = id
            ORDER BY ph.updated_at DESC
            LIMIT 1)
            """
    )
    private PriceHistory latestPrice;
}
