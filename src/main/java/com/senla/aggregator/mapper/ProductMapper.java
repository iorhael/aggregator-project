package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.product.ProductCreateDto;
import com.senla.aggregator.dto.product.ProductDetailedDto;
import com.senla.aggregator.dto.product.ProductInfoDto;
import com.senla.aggregator.dto.product.ProductNameDescriptionDto;
import com.senla.aggregator.dto.product.ProductPreviewDto;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductInfoDto toProductInfoDto(Product product);

    ProductNameDescriptionDto toProductNameDescriptionDto(Product product);

    Product toProduct(ProductCreateDto product);

    @Mapping(target = "vendorName", source = "vendor.name")
    @Mapping(target = "minimalPrice", source = "summary.minimalPrice")
    @Mapping(target = "offersCount", source = "summary.offersCount")
    @Mapping(target = "averageRating", source = "summary.averageRating")
    ProductPreviewDto toProductPreviewDto(Product product);

    @Mapping(target = "vendorName", source = "vendor.name")
    @Mapping(target = "minimalPrice", source = "summary.minimalPrice")
    @Mapping(target = "offersCount", source = "summary.offersCount")
    @Mapping(target = "commentsCount", source = "summary.commentsCount")
    @Mapping(target = "averageRating", source = "summary.averageRating")
    ProductDetailedDto toProductDetailedDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, ProductUpdateDto dto);
}
