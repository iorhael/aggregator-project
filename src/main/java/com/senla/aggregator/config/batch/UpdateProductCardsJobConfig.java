package com.senla.aggregator.config.batch;

import com.senla.aggregator.config.batch.helper.Constants;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class UpdateProductCardsJobConfig {

    @Bean
    public Job updateProductCardsJob(JobRepository jobRepository,
                                     Step updateProductCardsStep,
                                     JobExecutionListener fileCleanerJobExecutionListener) {
        return new JobBuilder(Constants.UPDATE_CARDS_JOB_NAME, jobRepository)
                .start(updateProductCardsStep)
                .listener(fileCleanerJobExecutionListener)
                .build();
    }

    @Bean
    public Step updateProductCardsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       FileItemReader<ProductCardImportDto> reader,
                                       BeanValidatingItemProcessor<ProductCardImportDto> beanValidatingItemProcessor,
                                       ItemWriter<ProductCardImportDto> updateProductCardsItemWriter) {
        return new StepBuilder(Constants.MAIN_STEP_NAME, jobRepository)
                .<ProductCardImportDto, ProductCardImportDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(beanValidatingItemProcessor)
                .writer(updateProductCardsItemWriter)
                .faultTolerant()
                .skip(DataIntegrityViolationException.class)
//                .listener(stepExceptionListener)
                .build();
    }
}
