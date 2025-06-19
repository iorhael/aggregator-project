package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.comment.CommentCreateDto;
import com.senla.aggregator.dto.comment.CommentGetDto;
import com.senla.aggregator.dto.comment.CommentUpdateDto;
import com.senla.aggregator.model.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorName", source = "author.username")
    CommentGetDto toCommentGetDto(Comment comment);

    Comment toComment(CommentCreateDto comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(@MappingTarget Comment comment, CommentUpdateDto dto);
}
