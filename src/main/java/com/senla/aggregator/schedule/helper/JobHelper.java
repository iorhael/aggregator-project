package com.senla.aggregator.schedule.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.senla.aggregator.config.batch.helper.Constants.SUPPORTED_CONTENT_TYPES;

@Slf4j
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

    public Optional<ContentType> getContentTypeOfFile(Path filePath) {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(filePath))) {
            String typeRepresentation = URLConnection.guessContentTypeFromStream(is);

            if (Objects.isNull(typeRepresentation) || isNotSupportedContentType(typeRepresentation)) {
                log.error("Unsupported file type: {}", typeRepresentation);
                return Optional.empty();
            }

            return Optional.of(ContentType.valueOf(typeRepresentation));
        } catch (IOException e) {
            log.error("Failed to read file: {}", filePath, e);
            return Optional.empty();
        }
    }

    public boolean isNotSupportedContentType(String typeRepresentation) {
        try {
            ContentType contentType = ContentType.fromValue(typeRepresentation);
            return !Arrays.stream(SUPPORTED_CONTENT_TYPES).toList()
                    .contains(contentType);
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
