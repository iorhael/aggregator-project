package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardDetailedDto;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import com.senla.aggregator.dto.productCard.ProductCardPreviewDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import com.senla.aggregator.model.ProductCard;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductCardMapper {

    ProductCard toProductCard(ProductCardCreateDto productCardCreateDto);

    ProductCard toProductCard(ProductCardImportDto productCardImportDto);

    @Mapping(source = "latestPrice.price", target = "price")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "retailer.name", target = "retailerName")
    @Mapping(source = "product.vendor.name", target = "vendorName")
    ProductCardDetailedDto toProductCardDetailedDto(ProductCard productCard);

    @Mapping(source = "latestPrice.price", target = "price")
    @Mapping(source = "retailer.name", target = "retailerName")
    ProductCardPreviewDto toProductCardPreviewDto(ProductCard productCard);

    @Mapping(source = "latestPrice.price", target = "price")
    @Mapping(source = "product.name", target = "productName")
    ProductCardImportDto toProductCardImportDto(ProductCard productCard);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductCard(@MappingTarget ProductCard productCard, ProductCardUpdateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductCardWithImportDto(@MappingTarget ProductCard productCard, ProductCardImportDto dto);
}
