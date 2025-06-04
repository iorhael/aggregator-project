package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "batchLogger")
public class ProductCardSkipListener implements SkipListener<ProductCardImportDto, ProductCardImportDto> {

    @Override
    public void onSkipInProcess(ProductCardImportDto product, Throwable exception) {
        log.warn("Item was skipped: {}. Reason: {}", product.getProductName(), exception.getMessage());
    }
}
