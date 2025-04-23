package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.favorite.FavoriteCreateDto;
import com.senla.aggregator.dto.favorite.FavoriteGetDto;
import com.senla.aggregator.dto.favorite.FavoriteUpdateDto;
import com.senla.aggregator.model.Favorite;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    Favorite toFavorite(FavoriteCreateDto favorite);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.verified", target = "verified")
    @Mapping(source = "product.vendor.name", target = "vendorName")
    @Mapping(source = "product.summary.offersCount", target = "offersCount")
    @Mapping(source = "product.summary.minimalPrice", target = "minimalPrice")
    @Mapping(source = "product.summary.averageRating", target = "averageRating")
    FavoriteGetDto toFavoriteGetDto(Favorite favorite);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFavorite(@MappingTarget Favorite favorite, FavoriteUpdateDto dto);
}
