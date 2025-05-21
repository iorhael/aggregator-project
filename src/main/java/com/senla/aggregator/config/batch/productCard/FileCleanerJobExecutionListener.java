package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.util.FileUtil;
import lombok.NonNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import static com.senla.aggregator.config.batch.helper.Constants.IMPORT_FILE_PARAM;

@Component
public class FileCleanerJobExecutionListener implements JobExecutionListener {

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        String path = jobExecution.getJobParameters().getString(IMPORT_FILE_PARAM);
        FileUtil.deleteFileWithoutException(path);
    }
}
