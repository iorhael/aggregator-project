package com.senla.aggregator.config.batch;

import com.senla.aggregator.controller.helper.ContentType;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;

public interface FileItemReader<T> extends ItemReader<T>, ItemStream {
    ContentType getContentType();
}
