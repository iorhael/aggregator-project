package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.config.batch.FileItemReader;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.senla.aggregator.config.batch.helper.Constants.INVALID_CONTENT_TYPE;

@Configuration
public class CommonProductCardJobConfig {

    @Bean
    @StepScope
    public FileItemReader<ProductCardImportDto> reader(List<FileItemReader<ProductCardImportDto>> readers,
                                                       @Value("#{jobParameters['contentType']}") String contentType) {
        return readers.stream()
                .filter(r ->
                        r.getContentType().equals(ContentType.fromValue(contentType)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(INVALID_CONTENT_TYPE, contentType)));
    }
}
