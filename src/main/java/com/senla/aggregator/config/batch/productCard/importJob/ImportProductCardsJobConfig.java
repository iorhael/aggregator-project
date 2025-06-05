package com.senla.aggregator.config.batch.productCard.importJob;

import com.senla.aggregator.config.batch.FileItemReader;
import com.senla.aggregator.config.batch.productCard.ProductCardCompositeItemProcessor;
import com.senla.aggregator.config.batch.productCard.UniqueProductNameFilterProcessor;
import com.senla.aggregator.dto.OnCreateGroup;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Configuration
public class ImportProductCardsJobConfig {

    @Bean
    public ItemProcessor<ProductCardImportDto, ProductCardImportDto> importProcessor(
            UniqueProductNameFilterProcessor uniqueProcessor) {
        return new ProductCardCompositeItemProcessor(List.of(uniqueProcessor), OnCreateGroup.class);
    }

    @Bean
    public Job importProductCardsJob(JobRepository jobRepository,
                                     Step createProductCardsStep,
                                     Step importSendLogStep,
                                     JobExecutionListener fileCleanerJobExecutionListener,
                                     JobExecutionListener productCardLoggingJobListener) {
        return new JobBuilder(IMPORT_CARDS_JOB_NAME, jobRepository)
                .start(createProductCardsStep)
                .on("*").to(importSendLogStep)
                .end()
                .listener(fileCleanerJobExecutionListener)
                .listener(productCardLoggingJobListener)
                .build();
    }

    @Bean
    public Step importSendLogStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  Tasklet sendLogTasklet,
                                  StepExecutionListener importJobPrepareArgsForSendStepExecutionListener) {
        return new StepBuilder(SEND_LOG_STEP_NAME, jobRepository)
                .tasklet(sendLogTasklet, transactionManager)
                .listener(importJobPrepareArgsForSendStepExecutionListener)
                .build();
    }

    @Bean
    public Step createProductCardsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       FileItemReader<ProductCardImportDto> reader,
                                       ItemProcessor<ProductCardImportDto, ProductCardImportDto> importProcessor,
                                       ItemWriter<ProductCardImportDto> createProductCardsItemWriter,
                                       StepExecutionListener exceptionMainStepExecutionListener) {
        return new StepBuilder(MAIN_STEP_NAME, jobRepository)
                .<ProductCardImportDto, ProductCardImportDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(importProcessor)
                .writer(createProductCardsItemWriter)
                .faultTolerant()
                .listener(exceptionMainStepExecutionListener)
                .build();
    }
}
