package com.senla.aggregator.repository;

import com.senla.aggregator.model.Vendor;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    @NonNull
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<Vendor> findAll(@NonNull Pageable pageable);
}
