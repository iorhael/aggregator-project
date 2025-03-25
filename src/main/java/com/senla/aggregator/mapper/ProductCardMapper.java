package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardGetDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import com.senla.aggregator.mapper.exception.MappingException;
import com.senla.aggregator.model.PriceHistory;
import com.senla.aggregator.model.ProductCard;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static com.senla.aggregator.mapper.exception.ExceptionMessages.*;

@Mapper(componentModel = "spring")
public interface ProductCardMapper {

    ProductCard toProductCard(ProductCardCreateDto productCardCreateDto);

    @Mapping(source = "retailer.name", target = "retailerName")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "priceHistories", target = "price", qualifiedByName = "mapPriceHistoriesToActualPrice")
    ProductCardGetDto toProductCardGetDto(ProductCard productCard);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductCard(@MappingTarget ProductCard productCard, ProductCardUpdateDto dto);

    @Named("mapPriceHistoriesToActualPrice")
    default BigDecimal mapPriceHistoriesToActualPrice(List<PriceHistory> priceHistories) {
        return priceHistories.stream()
                .max(Comparator.comparing(PriceHistory::getUpdatedAt))
                .map(PriceHistory::getPrice)
                .orElseThrow(() -> new MappingException(PRICE_NOT_FOUND));
    }
}
