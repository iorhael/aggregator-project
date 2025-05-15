package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;

public interface ProductCardItemWriter extends ItemWriter<ProductCardImportDto>, ItemStream {
    ContentType getContentType();
}
