package com.senla.aggregator.controller;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.comment.CommentCreateDto;
import com.senla.aggregator.dto.comment.CommentGetDto;
import com.senla.aggregator.dto.comment.CommentUpdateDto;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.COMMENT;
import static com.senla.aggregator.controller.helper.Constants.DELETION_MESSAGE;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
@Tag(name = "Comments Resource", description = "Manage user comments on products")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Get comment by ID",
            description = "Retrieve a single comment by its unique identifier"
    )
    @GetMapping("/{id}")
    public CommentGetDto findComment(@PathVariable UUID id) {
        return commentService.getComment(id);
    }

    @Operation(
            summary = "Get comment of user",
            description = "Retrieve user comment under specific product"
    )
    @GetMapping("/my/{productId}")
    public CommentGetDto findMyComment(@PathVariable UUID productId, Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());
        return commentService.getMyComment(productId, authorId);
    }

    @Operation(
            summary = "Get all comments of a product",
            description = "Retrieve paginated list of comments for the given product ID"
    )
    @GetMapping("/products/{productId}")
    public PaginationStatsDto<CommentGetDto> findCommentsOfProduct(@PathVariable UUID productId,
                                                                   @RequestParam(defaultValue = "0") int pageNo,
                                                                   @RequestParam(defaultValue = "15") int pageSize) {
        return commentService.getCommentsOfProduct(productId, pageNo, pageSize);
    }

    @Operation(
            summary = "Create a new comment",
            description = "Allows an authenticated user to add a comment to a product"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentGetDto createComment(@Valid @RequestBody CommentCreateDto dto,
                                       Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());
        return commentService.createComment(dto, authorId);
    }

    @Operation(
            summary = "Update an existing comment",
            description = "Allows the original author to edit their comment"
    )
    @PutMapping("/{id}")
    public CommentGetDto updateComment(@Valid @RequestBody CommentUpdateDto dto,
                                       @PathVariable UUID id,
                                       Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());
        return commentService.updateComment(dto, id, authorId);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Allows the comment author or an admin to delete the comment"
    )
    @DeleteMapping("/{id}")
    public ResponseInfoDto deleteComment(@PathVariable UUID id, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.ADMIN.getPrefixedRole()));
        UUID authorId = UUID.fromString(authentication.getName());

        if (isAdmin) commentService.deleteComment(id);
        else commentService.checkOwnershipAndDeleteComment(id, authorId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, COMMENT, id))
                .build();
    }
}
