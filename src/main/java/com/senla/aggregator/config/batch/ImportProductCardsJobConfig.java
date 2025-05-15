package com.senla.aggregator.config.batch;

import com.senla.aggregator.config.batch.helper.Constants;
import com.senla.aggregator.config.batch.helper.StepExceptionListener;
import com.senla.aggregator.config.batch.helper.ValidatingItemProcessorWithGroups;
import com.senla.aggregator.config.batch.productCard.ProductCardItemReader;
import com.senla.aggregator.dto.OnCreateGroup;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportProductCardsJobConfig {

    @Bean
    public ValidatingItemProcessorWithGroups<ProductCardImportDto> createProductCardsValidatingProcessor() {
        ValidatingItemProcessorWithGroups<ProductCardImportDto> processor =
                new ValidatingItemProcessorWithGroups<>(OnCreateGroup.class);
        processor.setFilter(true);

        return processor;
    }

    @Bean
    public Job importProductCardsJob(JobRepository jobRepository,
                                     Step createProductCardsStep,
                                     JobExecutionListener fileCleanerJobExecutionListener) {
        return new JobBuilder(Constants.IMPORT_CARDS_JOB_NAME, jobRepository)
                .start(createProductCardsStep)
                .listener(fileCleanerJobExecutionListener)
                .build();
    }

    @Bean
    public Step createProductCardsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       ProductCardItemReader reader,
                                       ItemWriter<ProductCardImportDto> createProductCardsItemWriter,
                                       StepExceptionListener stepExceptionListener) {
        return new StepBuilder(Constants.MAIN_STEP_NAME, jobRepository)
                .<ProductCardImportDto, ProductCardImportDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(createProductCardsValidatingProcessor())
                .writer(createProductCardsItemWriter)
                .faultTolerant()
                .skip(DataIntegrityViolationException.class)
                .listener(stepExceptionListener)
                .build();
    }
}
