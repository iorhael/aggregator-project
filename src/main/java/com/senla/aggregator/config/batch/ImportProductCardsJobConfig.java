package com.senla.aggregator.config.batch;

import com.senla.aggregator.config.batch.helper.Constants;
import com.senla.aggregator.config.batch.helper.ValidatingItemProcessorWithGroups;
import com.senla.aggregator.config.batch.productCard.UniqueProductNameFilterProcessor;
import com.senla.aggregator.dto.OnCreateGroup;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.xml.bind.ValidationException;
import java.util.List;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Configuration
public class ImportProductCardsJobConfig {

    @Bean
    public ItemProcessor<ProductCardImportDto, ProductCardImportDto> productCardCompositeProcessor(
            UniqueProductNameFilterProcessor uniqueProcessor) {
        ValidatingItemProcessorWithGroups<ProductCardImportDto> validatingProcessor =
                new ValidatingItemProcessorWithGroups<>(OnCreateGroup.class);
        validatingProcessor.setFilter(true);

        CompositeItemProcessor<ProductCardImportDto, ProductCardImportDto> compositeProcessor =
                new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(List.of(
               validatingProcessor,
               uniqueProcessor
        ));

        return compositeProcessor;
    }

    @Bean
    public Job importProductCardsJob(JobRepository jobRepository,
                                     Step createProductCardsStep,
                                     Step sendLogStep,
                                     JobExecutionListener fileCleanerJobExecutionListener,
                                     JobExecutionListener jobBenchmarkListener) {
        return new JobBuilder(IMPORT_CARDS_JOB_NAME, jobRepository)
                .start(createProductCardsStep)
                .on("*").to(sendLogStep)
                .end()
                .listener(fileCleanerJobExecutionListener)
                .listener(jobBenchmarkListener)
                .build();
    }

    @Bean
    public Step sendLogStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            Tasklet sendLogTasklet) {
        return new StepBuilder("some_name", jobRepository)
                .tasklet(sendLogTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step createProductCardsStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       FileItemReader<ProductCardImportDto> reader,
                                       ItemProcessor<ProductCardImportDto, ProductCardImportDto> productCardCompositeProcessor,
                                       ItemWriter<ProductCardImportDto> createProductCardsItemWriter,
                                       SkipListener<ProductCardImportDto, ProductCardImportDto> productCardSkipListener,
                                       StepExecutionListener exceptionStepExecutionListener) {
        return new StepBuilder(MAIN_STEP_NAME, jobRepository)
                .<ProductCardImportDto, ProductCardImportDto>chunk(1000, transactionManager)
                .reader(reader)
                .processor(productCardCompositeProcessor)
                .writer(createProductCardsItemWriter)
                .faultTolerant()
                .skip(ValidationException.class)
                .listener(productCardSkipListener)
                .listener(exceptionStepExecutionListener)
                .build();
    }
}
