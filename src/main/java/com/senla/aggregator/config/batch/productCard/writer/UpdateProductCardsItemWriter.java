package com.senla.aggregator.config.batch.productCard.writer;

import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import com.senla.aggregator.mapper.ProductCardMapper;
import com.senla.aggregator.model.PriceHistory;
import com.senla.aggregator.model.ProductCard;
import com.senla.aggregator.repository.PriceHistoryRepository;
import com.senla.aggregator.repository.ProductCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class UpdateProductCardsItemWriter implements ItemWriter<ProductCardImportDto> {

    private final ProductCardRepository productCardRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductCardMapper productCardMapper;

    @Value("#{jobParameters['retailerId']}")
    private String retailerId;

    private Map<String, ProductCard> oldCardsMap;

    @BeforeWrite
    public void beforeWrite(Chunk<? extends ProductCardImportDto> chunk) {
        List<String> productNames = chunk.getItems()
                .stream()
                .map(ProductCardImportDto::getProductName)
                .toList();

        oldCardsMap = productCardRepository.findByProductNamesAndRetailerId(productNames, UUID.fromString(retailerId))
                .stream()
                .collect(Collectors.toMap(
                        pc -> pc.getProduct().getName(),
                        Function.identity())
                );
    }

    @Override
    public void write(Chunk<? extends ProductCardImportDto> chunk) {
        List<PriceHistory> prices = chunk.getItems()
                .stream()
                .map(this::updateProduct)
                .flatMap(Optional::stream)
                .toList();

        priceHistoryRepository.saveAll(prices);
    }

    private Optional<PriceHistory> updateProduct(ProductCardImportDto dto) {
        ProductCard card = oldCardsMap.get(dto.getProductName());
        if (Objects.isNull(card)) return Optional.empty();

        productCardMapper.updateProductCardWithImportDto(card, dto);

        if (Objects.isNull(dto.getPrice())) return Optional.empty();
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setCard(card);
        priceHistory.setPrice(dto.getPrice());

        return Optional.of(priceHistory);
    }
}
