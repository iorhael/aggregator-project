package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@StepScope
@Slf4j(topic = "batchLogger")
public class UniqueProductNameFilterProcessor implements ItemProcessor<ProductCardImportDto, ProductCardImportDto> {

    private final Set<String> productNames = ConcurrentHashMap.newKeySet();

    @Override
    public ProductCardImportDto process(ProductCardImportDto item) {
        String productName = item.getProductName();

        if (productNames.contains(productName)) {
            log.warn("Item was skipped {}. Reason: Duplicate product name", item);
            return null;
        }
        productNames.add(productName);

        return item;
    }
}
