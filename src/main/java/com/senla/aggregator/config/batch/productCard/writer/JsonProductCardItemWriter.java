package com.senla.aggregator.config.batch.productCard.writer;

import com.senla.aggregator.config.batch.FileItemWriter;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.senla.aggregator.config.batch.helper.Constants.JSON_EXPORT_WRITER_NAME;

@Component
@StepScope
public class JsonProductCardItemWriter implements FileItemWriter<ProductCardImportDto> {

    private final JsonFileItemWriter<ProductCardImportDto> delegate;

    public JsonProductCardItemWriter(@Value("#{jobParameters['exportFile']}") String outputFilePath) {
        WritableResource resource = new FileSystemResource(outputFilePath);

        JacksonJsonObjectMarshaller<ProductCardImportDto> marshaller =
                new JacksonJsonObjectMarshaller<>();

        this.delegate = new JsonFileItemWriterBuilder<ProductCardImportDto>()
                .name(JSON_EXPORT_WRITER_NAME)
                .resource(resource)
                .jsonObjectMarshaller(marshaller)
                .build();
    }

    @Override
    public void write(@NonNull Chunk<? extends ProductCardImportDto> chunk) throws Exception {
        delegate.write(chunk);
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
