package com.senla.aggregator.util;

import com.senla.aggregator.dto.PaginationStatsDto;
import org.springframework.data.domain.Page;

import java.util.List;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static <T> PaginationStatsDto<T> convertToPaginationStatsDto(Page<T> contentPage) {
        return new PaginationStatsDto<>(
                contentPage.toList(),
                contentPage.getTotalElements(),
                contentPage.getTotalPages(),
                contentPage.getNumber(),
                contentPage.getSize()
        );
    }

    public static <T> PaginationStatsDto<T> convertToPaginationStatsDto(List<T> content,
                                                                        long totalElements,
                                                                        int pageSize,
                                                                        int pageNo) {
        return new PaginationStatsDto<>(
                content,
                totalElements,
                (totalElements + pageSize - 1) / pageSize,
                pageNo,
                pageSize
        );
    }
}
