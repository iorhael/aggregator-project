package com.senla.aggregator.service.review;

import com.senla.aggregator.dto.review.ReviewCreateDto;
import com.senla.aggregator.dto.review.ReviewGetDto;
import com.senla.aggregator.dto.review.ReviewUpdateDto;
import com.senla.aggregator.mapper.ReviewMapper;
import com.senla.aggregator.model.Review;
import com.senla.aggregator.model.Review_;
import com.senla.aggregator.repository.product.ProductRepository;
import com.senla.aggregator.repository.review.ReviewRepository;
import com.senla.aggregator.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.REVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ReviewMapper reviewMapper;

    @Override
    public ReviewGetDto getReview(UUID id) {
        return reviewRepository.findWithAuthorAndProductById(id)
                .map(reviewMapper::toReviewGetDto)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND));
    }

    @Override
    @Transactional
    public ReviewGetDto createReview(ReviewCreateDto dto, UUID authorId) {
        Review review = reviewMapper.toReview(dto);

        review.setAuthor(userRepository.getReferenceById(authorId));
        review.setProduct(productRepository.getReferenceById(dto.getProductId()));

        return reviewMapper.toReviewGetDto(reviewRepository.save(review));
    }

    @Override
    public List<ReviewGetDto> getReviewsOfProduct(UUID productId, int pageNo, int pageSize) {
        return reviewRepository.findAllWithAuthorAndProductByProductId(productId, PageRequest.of(pageNo, pageSize,
                        Sort.by(Sort.Direction.DESC, Review_.CREATED_AT)))
                .stream()
                .map(reviewMapper::toReviewGetDto)
                .toList();
    }

    @Override
    public List<ReviewGetDto> getReviewsOfAuthor(String authorName, int pageNo, int pageSize) {
        return reviewRepository.findAllWithAuthorAndProductByAuthorUsername(authorName, PageRequest.of(pageNo, pageSize,
                        Sort.by(Sort.Direction.DESC, Review_.CREATED_AT)))
                .stream()
                .map(reviewMapper::toReviewGetDto)
                .toList();
    }


    @Override
    @Transactional
    public ReviewGetDto updateReview(ReviewUpdateDto dto, UUID reviewId, UUID authorId) {
        Review review = reviewRepository.findWithAuthorAndProductByIdAndAuthorId(reviewId, authorId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND));

        reviewMapper.updateReview(review, dto);

        return reviewMapper.toReviewGetDto(review);
    }

    @Override
    @Transactional
    public void checkOwnershipAndDeleteReview(UUID reviewId, UUID authorId) {
        reviewRepository.deleteByIdAndAuthorId(reviewId, authorId);
    }

    @Override
    public void deleteReview(UUID reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
