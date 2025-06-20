package com.senla.aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TempReviewImageDto {
    private UUID id;
    private String imageUrl;
}
