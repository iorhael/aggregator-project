package com.senla.aggregator.service.retailer;

import com.senla.aggregator.dto.retailer.RetailerCreateDto;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.dto.retailer.RetailerUpdateDto;

import java.util.List;
import java.util.UUID;

public interface RetailerService {

    RetailerGetDto registerRetailer(RetailerCreateDto retailer, UUID ownerId);

    RetailerGetDto getRetailerByOwnerId(UUID id);

    List<RetailerGetDto> getAllRetailers(int pageNo, int pageSize);

    RetailerGetDto updateRetailer(RetailerUpdateDto retailer, UUID ownerId);

    void deleteRetailer(UUID ownerId);
}
