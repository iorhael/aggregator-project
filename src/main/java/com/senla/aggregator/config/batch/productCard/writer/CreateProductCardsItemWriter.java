package com.senla.aggregator.config.batch.productCard.writer;

import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import com.senla.aggregator.mapper.ProductCardMapper;
import com.senla.aggregator.model.PriceHistory;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.ProductCard;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.repository.PriceHistoryRepository;
import com.senla.aggregator.repository.ProductCardRepository;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.RetailerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
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
public class CreateProductCardsItemWriter implements ItemWriter<ProductCardImportDto> {

    private final ProductRepository productRepository;
    private final RetailerRepository retailerRepository;
    private final ProductCardRepository productCardRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductCardMapper productCardMapper;

    @Value("#{jobParameters['retailerId']}")
    private String retailerId;

    @Value("#{jobParameters['verfiedProductsOnly'] ?: false}")
    private Boolean verifiedProductsOnly;

    private Retailer retailer;
    private Map<String, Product> productMap;

    @BeforeWrite
    public void beforeWrite(Chunk<? extends ProductCardImportDto> chunk) {
        List<String> productNames = chunk.getItems()
                .stream()
                .map(ProductCardImportDto::getProductName)
                .toList();

        productMap = productRepository.findByNames(productNames, verifiedProductsOnly)
                .stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        Function.identity())
                );

        retailer = retailerRepository.getReferenceById(UUID.fromString(retailerId));
    }

    @Override
    public void write(Chunk<? extends ProductCardImportDto> chunk) {
        List<Map.Entry<ProductCard, PriceHistory>> pairs = chunk.getItems()
                .stream()
                .map(this::createPair)
                .flatMap(Optional::stream)
                .toList();

        productCardRepository.saveAll(pairs.stream()
                .map(Map.Entry::getKey)
                .toList()
        );

        priceHistoryRepository.saveAll(pairs.stream()
                .map(Map.Entry::getValue)
                .toList()
        );
    }

    private Optional<Map.Entry<ProductCard, PriceHistory>> createPair(ProductCardImportDto dto) {
        Product product = productMap.get(dto.getProductName());
        if (Objects.isNull(product)) return Optional.empty();

        ProductCard card = productCardMapper.toProductCard(dto);
        card.setProduct(product);
        card.setRetailer(retailer);

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setCard(card);
        priceHistory.setPrice(dto.getPrice());

        return Optional.of(new AbstractMap.SimpleEntry<>(card, priceHistory));
    }
}
