package com.senla.aggregator.schedule.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.senla.aggregator.config.batch.helper.Constants.SUPPORTED_CONTENT_TYPES;
import static com.senla.aggregator.util.CommonConstants.TMP_FILE_EXTENSION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobHelper {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

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

    public File downloadFileByLink(String downloadLink, String filePrefix) {
        return restTemplate.execute(downloadLink, HttpMethod.GET, null,
                clientHttpResponse -> {
                    File ret = File.createTempFile(filePrefix, TMP_FILE_EXTENSION);
                    StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                    return ret;
                }
        );
    }

    public boolean isFileTooLarge(String downloadLink) {
        HttpHeaders headers = restTemplate.headForHeaders(downloadLink);
        long fileSize = headers.getContentLength();

        return fileSize == -1 || fileSize > maxFileSize.toBytes();
    }

    public Optional<ContentType> getSupportedContentType(String downloadLink) {
        HttpHeaders headers = restTemplate.headForHeaders(downloadLink);
        MediaType type = headers.getContentType();

        if (Objects.isNull(type)) return Optional.empty();

        try {
            ContentType contentType = ContentType.fromValue(type.getType());
            if (!Arrays.stream(SUPPORTED_CONTENT_TYPES).toList()
                    .contains(contentType)) return Optional.empty();

            return Optional.of(contentType);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
