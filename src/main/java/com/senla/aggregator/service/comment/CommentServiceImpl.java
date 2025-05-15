package com.senla.aggregator.service.comment;

import com.senla.aggregator.dto.comment.CommentCreateDto;
import com.senla.aggregator.dto.comment.CommentGetDto;
import com.senla.aggregator.dto.comment.CommentUpdateDto;
import com.senla.aggregator.mapper.CommentMapper;
import com.senla.aggregator.model.Comment;
import com.senla.aggregator.model.Comment_;
import com.senla.aggregator.repository.CommentRepository;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentGetDto getComment(UUID id) {
        return commentRepository.findWithAuthorById(id)
                .map(commentMapper::toCommentGetDto)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND));
    }

    @Override
    @Transactional
    public CommentGetDto createComment(CommentCreateDto dto, UUID authorId) {
        Comment comment = commentMapper.toComment(dto);

        comment.setAuthor(userRepository.getReferenceById(authorId));
        comment.setProduct(productRepository.getReferenceById(dto.getProductId()));

        return commentMapper.toCommentGetDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentGetDto> getCommentsOfProduct(UUID productId, int pageNo, int pageSize) {
        return commentRepository.findAllWithAuthorByProductId(productId, PageRequest.of(pageNo, pageSize,
                        Sort.by(Sort.Direction.DESC, Comment_.CREATED_AT)))
                .stream()
                .map(commentMapper::toCommentGetDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentGetDto updateComment(CommentUpdateDto dto, UUID commentId, UUID authorId) {
        Comment comment = commentRepository.findWithAuthorByIdAndAuthorId(commentId, authorId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND));

        commentMapper.updateComment(comment, dto);

        return commentMapper.toCommentGetDto(comment);
    }

    @Override
    @Transactional
    public void checkOwnershipAndDeleteComment(UUID commentId, UUID authorId) {
        commentRepository.deleteByIdAndAuthorId(commentId, authorId);
    }

    @Override
    public void deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }
}
