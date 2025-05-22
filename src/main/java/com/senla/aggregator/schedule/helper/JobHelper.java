package com.senla.aggregator.schedule.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.aggregator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JobHelper {

    private final ObjectMapper objectMapper;

    public File generateReportFile(Object fileContent,
                                   String fileName,
                                   String fileExtension) throws IOException {
        File reportFile = File.createTempFile(fileName, fileExtension);

        try {
            objectMapper.writeValue(reportFile, fileContent);
        } catch (IOException e) {
            FileUtil.deleteFileWithoutException(reportFile.getAbsolutePath());
            throw e;
        }

        return reportFile;
    }
}
