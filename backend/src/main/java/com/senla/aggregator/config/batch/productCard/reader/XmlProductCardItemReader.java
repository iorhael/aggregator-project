package com.senla.aggregator.config.batch.productCard.reader;

import com.senla.aggregator.config.batch.FileItemReader;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.NonNull;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.senla.aggregator.config.batch.helper.Constants.PRODUCT_CARD_NAME;
import static com.senla.aggregator.config.batch.helper.Constants.XML_IMPORT_READER_NAME;

@Component
@StepScope
public class XmlProductCardItemReader implements FileItemReader<ProductCardImportDto> {

    private final StaxEventItemReader<ProductCardImportDto> delegate;

    public XmlProductCardItemReader(@Value("#{jobParameters['tempFile']}") String inputFilePath) {
        XStreamMarshaller unmarshaller = new XStreamMarshaller();
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put(PRODUCT_CARD_NAME, ProductCardImportDto.class);
        unmarshaller.setAliases(aliases);
        unmarshaller.getXStream().allowTypes(new Class[]{ProductCardImportDto.class});

        this.delegate = new StaxEventItemReaderBuilder<ProductCardImportDto>()
                .name(XML_IMPORT_READER_NAME)
                .resource(new FileSystemResource(inputFilePath))
                .addFragmentRootElements(PRODUCT_CARD_NAME)
                .unmarshaller(unmarshaller)
                .build();
    }

    @Override
    public ProductCardImportDto read() throws Exception {
        return delegate.read();
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
