package com.senla.aggregator.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public final class FileUtil {

    private FileUtil() {
    }

    public static void deleteFileWithoutException(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
