package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.product.ProductDetailedViewDto;
import com.senla.aggregator.dto.product.ProductSimpleViewDto;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.model.Category;
import com.senla.aggregator.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductSimpleViewDto product);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoriesToNames")
    ProductSimpleViewDto toProductSimpleViewDto(Product product);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoriesToNames")
    ProductDetailedViewDto toProductDetailedViewDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, ProductUpdateDto dto);

    @Named("mapCategoriesToNames")
    default List<String> mapCategoriesToStrings(List<Category> categories) {
        return categories.stream()
                .map(Category::getName)
                .toList();
    }
}
