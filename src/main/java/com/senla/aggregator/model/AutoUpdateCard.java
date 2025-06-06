package com.senla.aggregator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auto_update_cards")
@Getter
@Setter
public class AutoUpdateCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "download_link")
    private String downloadLink;

    @Column(name = "verified_products_only")
    private Boolean verifiedProductsOnly;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retailer_id")
    private Retailer retailer;
}
