package com.senla.aggregator.service.review;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.TempReviewImageDto;
import com.senla.aggregator.dto.review.ReviewCreateDto;
import com.senla.aggregator.dto.review.ReviewGetDto;
import com.senla.aggregator.dto.review.ReviewUpdateDto;
import com.senla.aggregator.mapper.ReviewMapper;
import com.senla.aggregator.model.Review;
import com.senla.aggregator.model.Review_;
import com.senla.aggregator.model.TempReviewImage;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.ReviewRepository;
import com.senla.aggregator.repository.TempReviewImageRepository;
import com.senla.aggregator.repository.UserRepository;
import com.senla.aggregator.service.minio.MinioService;
import com.senla.aggregator.util.PaginationUtil;
import com.senla.aggregator.util.SanitizerUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.REVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final String REVIEW_IMAGE_DIRECTORY = "reviews";

    private final MinioService minioService;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final TempReviewImageRepository tempReviewImageRepository;

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
    @Transactional
    public TempReviewImageDto storeReviewImage(MultipartFile image) throws IOException {
        try (InputStream stream = image.getInputStream()) {
            String imageUrl = minioService.saveImage(
                    stream,
                    REVIEW_IMAGE_DIRECTORY,
                    ContentType.fromValue(image.getContentType())
            );

            TempReviewImage tempReviewImage = new TempReviewImage();
            tempReviewImage.setImageLink(imageUrl);

            UUID id = tempReviewImageRepository.save(tempReviewImage).getId();

            return new TempReviewImageDto(id, imageUrl);
        }
    }

    @Override
    @Transactional
    public ReviewGetDto createReview(ReviewCreateDto dto, UUID authorId) {
        Review review = reviewMapper.toReview(dto);

        String processedContent = SanitizerUtil.convertMarkdownToSafeHtml(review.getContent());

        review.setContent(processedContent);
        review.setAuthor(userRepository.getReferenceById(authorId));
        review.setProduct(productRepository.getReferenceById(dto.getProductId()));

        tempReviewImageRepository.deleteAllById(dto.getImageIds());

        return reviewMapper.toReviewGetDto(reviewRepository.save(review));
    }

    @Override
    public PaginationStatsDto<ReviewGetDto> getReviewsOfProduct(UUID productId, int pageNo, int pageSize) {
        Page<ReviewGetDto> content = reviewRepository.findAllWithAuthorAndProductByProductId(
                productId,
                PageRequest.of(pageNo, pageSize,
                        Sort.by(Sort.Direction.DESC, Review_.CREATED_AT)
                )).map(reviewMapper::toReviewGetDto);

        return PaginationUtil.convertToPaginationStatsDto(content);
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

        if (Objects.nonNull(review.getContent()))
            review.setContent(SanitizerUtil.convertMarkdownToSafeHtml(review.getContent()));

        reviewMapper.updateReview(review, dto);
        tempReviewImageRepository.deleteAllById(dto.getImageIds());

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
