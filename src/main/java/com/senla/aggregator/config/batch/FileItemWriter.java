package com.senla.aggregator.config.batch;

import com.senla.aggregator.controller.helper.ContentType;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;

public interface FileItemWriter<T> extends ItemWriter<T>, ItemStream {
    ContentType getContentType();
}
