package com.senla.aggregator.config.batch.productCard.updateJob;

import com.senla.aggregator.config.batch.FileItemReader;
import com.senla.aggregator.config.batch.productCard.ProductCardCompositeItemProcessor;
import com.senla.aggregator.config.batch.productCard.UniqueProductNameFilterProcessor;
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
public class UpdateProductCardsJobConfig {

    @Bean
    public ItemProcessor<ProductCardImportDto, ProductCardImportDto> updateProcessor(
            UniqueProductNameFilterProcessor uniqueProcessor) {
        return new ProductCardCompositeItemProcessor(List.of(uniqueProcessor));
    }

    @Bean
    public Job updateProductCardsJob(JobRepository jobRepository,
                                     Step updateProductCardsStep,
                                     Step updateSendLogStep,
                                     JobExecutionListener fileCleanerJobExecutionListener,
                                     JobExecutionListener productCardLoggingJobListener) {
        return new JobBuilder(UPDATE_CARDS_JOB_NAME, jobRepository)
                .start(updateProductCardsStep)
                .on("*").to(updateSendLogStep)
                .end()
                .listener(fileCleanerJobExecutionListener)
                .listener(productCardLoggingJobListener)
                .build();
    }

    @Bean
    public Step updateSendLogStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  Tasklet sendLogTasklet,
                                  StepExecutionListener updateJobPrepareArgsForSendStepExecutionListener) {
        return new StepBuilder(SEND_LOG_STEP_NAME, jobRepository)
                .tasklet(sendLogTasklet, transactionManager)
                .listener(updateJobPrepareArgsForSendStepExecutionListener)
                .build();
    }

    @Bean
    public Step updateProductCardsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       FileItemReader<ProductCardImportDto> reader,
                                       ItemProcessor<ProductCardImportDto, ProductCardImportDto> updateProcessor,
                                       ItemWriter<ProductCardImportDto> updateProductCardsItemWriter,
                                       StepExecutionListener exceptionMainStepExecutionListener) {
        return new StepBuilder(MAIN_STEP_NAME, jobRepository)
                .<ProductCardImportDto, ProductCardImportDto>chunk(1000, transactionManager)
                .reader(reader)
                .processor(updateProcessor)
                .writer(updateProductCardsItemWriter)
                .listener(exceptionMainStepExecutionListener)
                .build();
    }
}
