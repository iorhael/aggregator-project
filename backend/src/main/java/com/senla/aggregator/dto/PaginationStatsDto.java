package com.senla.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginationStatsDto<T> {

    @JsonProperty("content")
    private List<T> content;

    @JsonProperty("total_elements")
    private long totalElements;

    @JsonProperty("total_pages")
    private long totalPages;

    @JsonProperty("number")
    private int number;

    @JsonProperty("size")
    private int size;
}
