package com.senla.aggregator.service.comment;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.comment.CommentCreateDto;
import com.senla.aggregator.dto.comment.CommentGetDto;
import com.senla.aggregator.dto.comment.CommentUpdateDto;

import java.util.UUID;

public interface CommentService {

    CommentGetDto getComment(UUID id);

    CommentGetDto getMyComment(UUID productId, UUID authorId);

    CommentGetDto createComment(CommentCreateDto dto, UUID authorId);

    PaginationStatsDto<CommentGetDto> getCommentsOfProduct(UUID productId, int pageNo, int pageSize);

    CommentGetDto updateComment(CommentUpdateDto comment, UUID commentId, UUID authorId);

    void checkOwnershipAndDeleteComment(UUID commentId, UUID authorId);

    void deleteComment(UUID commentId);
}
