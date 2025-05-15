package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;

public interface ProductCardItemReader extends ItemReader<ProductCardImportDto>, ItemStream {
    ContentType getContentType();
}
