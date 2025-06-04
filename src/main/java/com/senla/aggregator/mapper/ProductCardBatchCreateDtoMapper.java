package com.senla.aggregator.mapper;


import com.senla.aggregator.dto.productCard.ProductCardBatchCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCardBatchCreateDtoMapper {
    ProductCardBatchCreateDto toDto(ProductCardImportDto productCardImportDto);
}
