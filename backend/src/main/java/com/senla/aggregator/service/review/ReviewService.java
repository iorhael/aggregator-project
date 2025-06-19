package com.senla.aggregator.service.review;

import com.senla.aggregator.dto.review.ReviewCreateDto;
import com.senla.aggregator.dto.review.ReviewGetDto;
import com.senla.aggregator.dto.review.ReviewUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ReviewService {

    ReviewGetDto getReview(UUID id);

    int getReviewCountOfProduct(UUID productId);

    String storeTempImage(MultipartFile file) throws IOException;

    ReviewGetDto createReview(ReviewCreateDto dto, UUID authorId);

    List<ReviewGetDto> getReviewsOfProduct(UUID productId, int pageNo, int pageSize);

    List<ReviewGetDto> getReviewsOfAuthor(String authorName, int pageNo, int pageSize);

    ReviewGetDto updateReview(ReviewUpdateDto review, UUID reviewId, UUID authorId);

    void checkOwnershipAndDeleteReview(UUID reviewId, UUID authorId);

    void deleteReview(UUID reviewId);
}
