package com.senla.aggregator.config.batch.productCard.writer;

import com.senla.aggregator.config.batch.FileItemWriter;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.senla.aggregator.config.batch.helper.Constants.*;
import static com.senla.aggregator.util.CommonConstants.COMMA;

@Component
@StepScope
public class CsvProductCardItemWriter implements FileItemWriter<ProductCardImportDto> {

    private final FlatFileItemWriter<ProductCardImportDto> delegate;

    public CsvProductCardItemWriter(@Value("#{jobParameters['tempFile']}") String outputFilePath) {
        WritableResource resource = new FileSystemResource(outputFilePath);

        BeanWrapperFieldExtractor<ProductCardImportDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(PRODUCT_CARD_FIELDS_MODEL);
        DelimitedLineAggregator<ProductCardImportDto> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setFieldExtractor(fieldExtractor);

        this.delegate = new FlatFileItemWriterBuilder<ProductCardImportDto>()
                .name(CSV_EXPORT_WRITER_NAME)
                .resource(resource)
                .lineAggregator(lineAggregator)
                .headerCallback(w -> w.write(String.join(COMMA, PRODUCT_CARD_FIELDS_FILE)))
                .build();
    }

    @Override
    public void write(@NonNull Chunk<? extends ProductCardImportDto> chunk) throws Exception {
        delegate.write(chunk);
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
