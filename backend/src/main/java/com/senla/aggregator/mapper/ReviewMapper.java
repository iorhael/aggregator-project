package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.review.ReviewCreateDto;
import com.senla.aggregator.dto.review.ReviewGetDto;
import com.senla.aggregator.dto.review.ReviewUpdateDto;
import com.senla.aggregator.model.Review;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "authorName", source = "author.username")
    ReviewGetDto toReviewGetDto(Review review);

    Review toReview(ReviewCreateDto review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReview(@MappingTarget Review review, ReviewUpdateDto dto);
}
