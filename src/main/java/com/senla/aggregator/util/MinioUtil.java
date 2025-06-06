package com.senla.aggregator.util;

import com.senla.aggregator.service.exception.MinioException;
import org.apache.commons.lang3.StringUtils;

import static com.senla.aggregator.service.exception.ExceptionMessages.INVALID_OBJECT_URL;

public final class MinioUtil {

    private MinioUtil() {
    }

    public static String getObjectPath(String objectUrl, String serverAndBucketUrl) {
        String objectPath = StringUtils.substringAfter(objectUrl, serverAndBucketUrl);

        if (StringUtils.isBlank(objectPath))
            throw new MinioException(String.format(INVALID_OBJECT_URL, objectUrl));

        return objectPath;
    }
}
