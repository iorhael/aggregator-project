package com.senla.aggregator.service.review;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.review.ReviewCreateDto;
import com.senla.aggregator.dto.review.ReviewGetDto;
import com.senla.aggregator.dto.review.ReviewUpdateDto;
import com.senla.aggregator.mapper.ReviewMapper;
import com.senla.aggregator.model.Review;
import com.senla.aggregator.model.Review_;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.ReviewRepository;
import com.senla.aggregator.repository.UserRepository;
import com.senla.aggregator.service.MarkdownService;
import com.senla.aggregator.service.minio.MinioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.REVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final String REVIEW_TEMP_IMAGE_DIRECTORY = "reviews/temp";

    private final MinioService minioService;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;
    private final MarkdownService markdownService;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    public ReviewGetDto getReview(UUID id) {
        return reviewRepository.findWithAuthorAndProductById(id)
                .map(reviewMapper::toReviewGetDto)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND));
    }

    @Override
    public int getReviewCountOfProduct(UUID productId) {
        return reviewRepository.countByProductId(productId);
    }

    @Override
    public String storeTempImage(MultipartFile image) throws IOException {
        try (InputStream stream = image.getInputStream()) {
            return minioService.saveImage(
                    stream,
                    REVIEW_TEMP_IMAGE_DIRECTORY,
                    ContentType.fromValue(image.getContentType())
            );
        }
    }

    @Override
    @Transactional
    public ReviewGetDto createReview(ReviewCreateDto dto, UUID authorId) {
        Review review = reviewMapper.toReview(dto);

        String processedContent = markdownService.markdownToSafeHtml(review.getContent());

        review.setContent(processedContent);
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
