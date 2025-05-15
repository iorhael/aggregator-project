package com.senla.aggregator.config.batch.productCard.writer;

import com.senla.aggregator.config.batch.productCard.ProductCardItemWriter;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.lang.NonNull;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Component
@StepScope
public class XmlProductCardItemWriter implements ProductCardItemWriter {

    private final StaxEventItemWriter<ProductCardImportDto> delegate;

    public XmlProductCardItemWriter(@Value("#{jobParameters['exportFile']}") String outputFilePath) {
        WritableResource resource = new FileSystemResource(outputFilePath);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(Map.of(PRODUCT_CARD_NAME, ProductCardImportDto.class));

        this.delegate = new StaxEventItemWriterBuilder<ProductCardImportDto>()
                .name(XML_EXPORT_WRITER_NAME)
                .resource(resource)
                .marshaller(marshaller)
                .rootTagName(PRODUCT_CARDS_NAME)
                .build();
    }

    @Override
    public void write(@NonNull Chunk<? extends ProductCardImportDto> chunk) throws Exception {
        delegate.write(chunk);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.XML;
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
