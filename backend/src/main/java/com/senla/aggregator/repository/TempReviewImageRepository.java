package com.senla.aggregator.repository;

import com.senla.aggregator.model.TempReviewImage;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TempReviewImageRepository extends CrudRepository<TempReviewImage, UUID> {
}
