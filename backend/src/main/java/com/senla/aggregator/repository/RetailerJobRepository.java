package com.senla.aggregator.repository;

import com.senla.aggregator.model.RetailerJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RetailerJobRepository extends JpaRepository<RetailerJob, UUID> {

    @Query("SELECT r.jobExecutionId FROM RetailerJob r WHERE r.retailer.id = :retailerId ORDER BY r.createdAt DESC")
    Page<Long> findRetailerJobExecutionIds(@Param("retailerId") UUID retailerId, Pageable pageable);
}
