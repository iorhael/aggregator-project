package com.senla.aggregator.config.batch;

import com.senla.aggregator.config.batch.helper.StepExceptionListener;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import com.senla.aggregator.mapper.ProductCardMapper;
import com.senla.aggregator.model.ProductCard;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Configuration
public class ExportProductCardsJobConfig {

    @Bean
    @StepScope
    public JpaPagingItemReader<ProductCard> productCardReader(@Value("#{jobParameters['retailerId']}") String retailerId,
                                                              EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<ProductCard> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("""
                SELECT pc
                FROM ProductCard pc
                JOIN FETCH pc.product p
                WHERE pc.retailer.id = :retailerId
                """);
        reader.setParameterValues(Map.of(RETAILER_ID_PARAM, UUID.fromString(retailerId)));
        reader.setPageSize(50);
        return reader;
    }

    @Bean
    public ItemProcessor<ProductCard, ProductCardImportDto> productCardProcessor(ProductCardMapper mapper) {
        return mapper::toProductCardImportDto;
    }

    @Bean
    @StepScope
    public FileItemWriter<ProductCardImportDto> writer(List<FileItemWriter<ProductCardImportDto>> writers,
                                                       @Value("#{jobParameters['contentType']}") String contentType) {
        return writers.stream()
                .filter(r ->
                        r.getContentType().equals(ContentType.valueOf(contentType)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(INVALID_CONTENT_TYPE, contentType)));
    }

    @Bean
    public Job exportProductCardsJob(JobRepository jobRepository,
                                     Step exportProductCardsStep) {
        return new JobBuilder(EXPORT_CARDS_JOB_NAME, jobRepository)
                .start(exportProductCardsStep)
                .build();
    }

    @Bean
    public Step exportProductCardsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       JpaPagingItemReader<ProductCard> reader,
                                       ItemProcessor<ProductCard, ProductCardImportDto> productCardProcessor,
                                       FileItemWriter<ProductCardImportDto> writer,
                                       StepExceptionListener stepExceptionListener) {
        return new StepBuilder(MAIN_STEP_NAME, jobRepository)
                .<ProductCard, ProductCardImportDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(productCardProcessor)
                .writer(writer)
                .listener(stepExceptionListener)
                .build();
    }
}
