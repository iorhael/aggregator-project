package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.review.ReviewCreateDto;
import com.senla.aggregator.dto.review.ReviewGetDto;
import com.senla.aggregator.dto.review.ReviewUpdateDto;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.service.review.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Messages.DELETION_MESSAGE;
import static com.senla.aggregator.controller.helper.Messages.REVIEW;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Resource", description = "CRUD")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ReviewGetDto findReview(@PathVariable UUID id) {
        return reviewService.getReview(id);
    }

    @GetMapping("/products/{productId}")
    public List<ReviewGetDto> findReviewsOfProduct(@PathVariable UUID productId,
                                                   @RequestParam(defaultValue = "0") int pageNo,
                                                   @RequestParam(defaultValue = "15") int pageSize) {
        return reviewService.getReviewsOfProduct(productId, pageNo, pageSize);
    }

    @GetMapping("/authors/{authorName}")
    public List<ReviewGetDto> findReviewsOfProduct(@PathVariable String authorName,
                                                   @RequestParam(defaultValue = "0") int pageNo,
                                                   @RequestParam(defaultValue = "15") int pageSize) {
        return reviewService.getReviewsOfAuthor(authorName, pageNo, pageSize);
    }

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewGetDto createReview(@Valid @RequestBody ReviewCreateDto dto,
                                     Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());

        return reviewService.createReview(dto, authorId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ReviewGetDto updateReview(@Valid @RequestBody ReviewUpdateDto dto,
                                     @PathVariable UUID id,
                                     Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());

        return reviewService.updateReview(dto, id, authorId);
    }

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
