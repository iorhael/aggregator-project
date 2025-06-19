package com.senla.aggregator.config.batch.productCard.reader;

import com.senla.aggregator.config.batch.FileItemReader;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.senla.aggregator.config.batch.helper.Constants.CSV_IMPORT_READER_NAME;
import static com.senla.aggregator.config.batch.helper.Constants.PRODUCT_CARD_FIELDS_FILE;
import static com.senla.aggregator.util.CommonConstants.COMMA;

@Component
@StepScope
public class CsvProductCardItemReader implements FileItemReader<ProductCardImportDto> {

    private final FlatFileItemReader<ProductCardImportDto> delegate;

    public CsvProductCardItemReader(@Value("#{jobParameters['tempFile']}") String inputFilePath) {
        this.delegate = new FlatFileItemReaderBuilder<ProductCardImportDto>()
                .name(CSV_IMPORT_READER_NAME)
                .resource(new FileSystemResource(inputFilePath))
                .linesToSkip(1)
                .delimited()
                .delimiter(COMMA)
                .names(PRODUCT_CARD_FIELDS_FILE)
                .targetType(ProductCardImportDto.class)
                .build();
    }

    @Override
    public ProductCardImportDto read() throws Exception {
        return delegate.read();
    }

    @Override
    public ContentType getContentType() {
        return ContentType.CSV;
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
