package com.senla.aggregator.controller;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.TempReviewImageDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.review.ReviewCreateDto;
import com.senla.aggregator.dto.review.ReviewGetDto;
import com.senla.aggregator.dto.review.ReviewUpdateDto;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.service.review.ReviewService;
import com.senla.aggregator.validation.fileTypes.AllowedFileTypes;
import com.senla.aggregator.validation.validImage.ValidImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.DELETION_MESSAGE;
import static com.senla.aggregator.controller.helper.Constants.REVIEW;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Resource", description = "CRUD operations for product reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Get review by ID",
            description = "Retrieve a single review by its unique identifier"
    )
    @GetMapping("/{id}")
    public ReviewGetDto findReview(@PathVariable UUID id) {
        return reviewService.getReview(id);
    }

    @Operation(
            summary = "Get reviews count by product id",
            description = "Retrieve count of reviews of the product"
    )
    @GetMapping("/count/{productId}")
    public int getReviewsCountOfProduct(@PathVariable UUID productId) {
        return reviewService.getReviewCountOfProduct(productId);
    }

    @Operation(
            summary = "Get product reviews",
            description = "Retrieve a paginated list of reviews for a specific product"
    )
    @GetMapping("/products/{productId}")
    public PaginationStatsDto<ReviewGetDto> findReviewsOfProduct(@PathVariable UUID productId,
                                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                                 @RequestParam(defaultValue = "15") int pageSize) {
        return reviewService.getReviewsOfProduct(productId, pageNo, pageSize);
    }

    @Operation(
            summary = "Store temp review image",
            description = "Store temp review image in minio",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            encoding = {
                                    @Encoding(name = "image", contentType = "image/png, image/jpeg")
                            }
                    )
            )
    )
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('AUTHOR')")
    public TempReviewImageDto storeReviewImage(@RequestPart(value = "image")
                                               @AllowedFileTypes(maxFileSize = 4096,
                                                       allowedFileTypes = {
                                                               ContentType.PNG,
                                                               ContentType.JPEG
                                                       })
                                               @ValidImage MultipartFile image) throws IOException {
        return reviewService.storeReviewImage(image);
    }

    @Operation(
            summary = "Get reviews by author",
            description = "Retrieve a paginated list of reviews written by a specific author"
    )
    @GetMapping("/authors/{authorName}")
    public List<ReviewGetDto> findReviewsOfAuthor(@PathVariable String authorName,
                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "15") int pageSize) {
        return reviewService.getReviewsOfAuthor(authorName, pageNo, pageSize);
    }

    @Operation(
            summary = "Create a review",
            description = "Submit a new review for a product. Requires author role."
    )
    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewGetDto createReview(@Valid @RequestBody ReviewCreateDto dto,
                                     Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());
        return reviewService.createReview(dto, authorId);
    }

    @Operation(
            summary = "Update a review",
            description = "Update an existing review. Requires author role and ownership."
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ReviewGetDto updateReview(@Valid @RequestBody ReviewUpdateDto dto,
                                     @PathVariable UUID id,
                                     Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());
        return reviewService.updateReview(dto, id, authorId);
    }

    @Operation(
            summary = "Delete a review",
            description = "Delete a review by ID. Admins can delete any review, authors can delete their own."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseInfoDto deleteReview(@PathVariable UUID id, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.ADMIN.getPrefixedRole()));
        UUID authorId = UUID.fromString(authentication.getName());

        if (isAdmin) reviewService.deleteReview(id);
        else reviewService.checkOwnershipAndDeleteReview(id, authorId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, REVIEW, id))
                .build();
    }
}
