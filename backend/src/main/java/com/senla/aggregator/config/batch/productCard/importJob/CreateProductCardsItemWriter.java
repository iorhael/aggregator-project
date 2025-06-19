package com.senla.aggregator.config.batch.productCard.importJob;

import com.senla.aggregator.dto.priceHistory.PriceHistoryBatchCreateDto;
import com.senla.aggregator.dto.product.ProductIdName;
import com.senla.aggregator.dto.productCard.ProductCardBatchCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardIdProductName;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import com.senla.aggregator.mapper.ProductCardMapper;
import com.senla.aggregator.repository.ProductRepository;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
@Slf4j(topic = "batchLogger")
public class CreateProductCardsItemWriter implements ItemWriter<ProductCardImportDto> {

    private final ProductCardMapper productCardMapper;
    private final ProductRepository productRepository;
    private final ProductCardRepository productCardRepository;
    private final PriceHistoryBatchRepository priceHistoryBatchRepository;

    @Value("#{jobParameters['retailerId']}")
    private String retailerId;

    @Value("#{jobParameters['verfiedProductsOnly'] ?: false}")
    private Boolean verifiedProductsOnly;

    private UUID retailerUUID;
    private List<String> productNames;
    private Map<String, UUID> productNameIdMap;
    private Map<String, BigDecimal> productNamePriceMap;
    private List<? extends ProductCardImportDto> importCards;

    @BeforeWrite
    public void beforeWrite(Chunk<? extends ProductCardImportDto> chunk) {
        importCards = chunk.getItems();
        retailerUUID = UUID.fromString(retailerId);
        initializeCollections();
    }

    @Override
    public void write(@NotNull Chunk<? extends ProductCardImportDto> chunk) {
        List<ProductCardBatchCreateDto> cards = importCards.stream()
                .map(this::filterAndConvertCards)
                .flatMap(Optional::stream)
                .toList();

        productCardRepository.batchUpsert(cards);

        Map<String, UUID> createdCardsProductNameIdMap = productCardRepository
                .findIdProductNames(productNames, retailerUUID)
                .stream()
                .collect(Collectors.toMap(
                        ProductCardIdProductName::getProductName,
                        ProductCardIdProductName::getId
                ));

        insertPrices(createdCardsProductNameIdMap);
    }

    private void initializeCollections() {
        productNames = importCards.stream()
                .map(ProductCardImportDto::getProductName)
                .toList();

        productNameIdMap = productRepository.findByNames(productNames, verifiedProductsOnly)
                .stream()
                .collect(Collectors.toMap(
                        ProductIdName::getName,
                        ProductIdName::getId)
                );

        productNamePriceMap = importCards.stream()
                .collect(Collectors.toMap(
                        ProductCardImportDto::getProductName,
                        ProductCardImportDto::getPrice)
                );
    }

    private Optional<ProductCardBatchCreateDto> filterAndConvertCards(ProductCardImportDto dto) {
        return Optional.ofNullable(productNameIdMap.get(dto.getProductName()))
                .map(productId -> {
                    ProductCardBatchCreateDto card = productCardMapper.toBatchCreateDto(dto);
                    card.setProductId(productId);
                    card.setRetailerId(retailerUUID);
                    return card;
                }).or(() -> {
                    log.warn("Item was skipped {}. Reason: Product with such name doesn't exist", dto);
                    return Optional.empty();
                });
    }

    private void insertPrices(Map<String, UUID> createdCardsProductNameIdMap) {
        List<PriceHistoryBatchCreateDto> prices = new ArrayList<>();

        productNamePriceMap.forEach((name, price) -> {
            UUID cardId = createdCardsProductNameIdMap.get(name);
            if (Objects.isNull(cardId)) {
                log.warn("Price wasn't inserted. Product with name {} doesn't exist", name);
                return;
            }
            prices.add(new PriceHistoryBatchCreateDto(cardId, price));
        });

        priceHistoryBatchRepository.batchInsert(prices);
    }
}
