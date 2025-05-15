package com.senla.aggregator.config.batch.productCard.reader;

import com.senla.aggregator.config.batch.productCard.ProductCardItemReader;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.senla.aggregator.config.batch.helper.Constants.JSON_IMPORT_READER_NAME;

@Component
@StepScope
public class JsonProductCardItemReader implements ProductCardItemReader {

    private final JsonItemReader<ProductCardImportDto> delegate;

    public JsonProductCardItemReader(@Value("#{jobParameters['importFile']}") String inputFilePath) {
        Resource resource = new FileSystemResource(inputFilePath);
        JacksonJsonObjectReader<ProductCardImportDto> objectReader =
                new JacksonJsonObjectReader<>(ProductCardImportDto.class);

        delegate = new JsonItemReaderBuilder<ProductCardImportDto>()
                .name(JSON_IMPORT_READER_NAME)
                .resource(resource)
                .jsonObjectReader(objectReader)
                .build();
    }

    @Override
    public ProductCardImportDto read() throws Exception {
        return delegate.read();
    }

    @Override
    public ContentType getContentType() {
        return ContentType.JSON;
    }

    @Override
    public void open(@NonNull ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(@NonNull ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
