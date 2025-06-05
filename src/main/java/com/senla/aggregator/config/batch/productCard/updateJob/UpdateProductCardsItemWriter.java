package com.senla.aggregator.config.batch.productCard.updateJob;

import com.senla.aggregator.dto.priceHistory.PriceHistoryBatchCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardBatchCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardBatchUpdateDto;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import com.senla.aggregator.mapper.ProductCardMapper;
import com.senla.aggregator.repository.priceHistory.PriceHistoryBatchRepository;
import com.senla.aggregator.repository.productCard.ProductCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
@Slf4j(topic = "batchLogger")
public class UpdateProductCardsItemWriter implements ItemWriter<ProductCardImportDto> {

    private final ProductCardRepository productCardRepository;
    private final ProductCardMapper productCardMapper;
    private final PriceHistoryBatchRepository priceHistoryBatchRepository;
    private final List<ProductCardBatchCreateDto> updatedCards = new ArrayList<>();
    private final List<PriceHistoryBatchCreateDto> newPrices = new ArrayList<>();

    @Value("#{jobParameters['retailerId']}")
    private String retailerId;

    private List<? extends ProductCardImportDto> importCards;
    private Map<String, ProductCardBatchUpdateDto> oldCardsMap;

    @BeforeWrite
    public void beforeWrite(Chunk<? extends ProductCardImportDto> chunk) {
        importCards = chunk.getItems();
        initializeCollections();
    }

    @Override
    public void write(@NotNull Chunk<? extends ProductCardImportDto> chunk) {
        formUpdatedCardsAndPrices();

        productCardRepository.batchUpdate(updatedCards);
        priceHistoryBatchRepository.batchInsert(newPrices);
    }

    private void initializeCollections() {
        List<String> productNames = importCards.stream()
                .map(ProductCardImportDto::getProductName)
                .toList();

        oldCardsMap = productCardRepository.findCardForBatchUpdate(productNames, UUID.fromString(retailerId))
                .stream()
                .collect(Collectors.toMap(
                        ProductCardBatchUpdateDto::getProductName,
                        Function.identity())
                );
    }

    private void formUpdatedCardsAndPrices() {
        for (ProductCardImportDto importCard : importCards) {
            ProductCardBatchUpdateDto card = oldCardsMap.get(importCard.getProductName());

            if (Objects.isNull(card)) {
                log.warn("Item was skipped {}. Reason: Product with such name doesn't exist", importCards);
                return;
            }

            productCardMapper.updateForBatch(card, importCard);
            ProductCardBatchCreateDto updatedCard = productCardMapper.toBatchCreateDto(card);
            updatedCards.add(updatedCard);

            if (Objects.nonNull(importCard.getPrice()))
                newPrices.add(new PriceHistoryBatchCreateDto(card.getId(), importCard.getPrice()));
        }
    }
}
