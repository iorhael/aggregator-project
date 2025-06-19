package com.senla.aggregator.service.retailer;

import com.senla.aggregator.dto.retailer.RetailerCreateDto;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.dto.retailer.RetailerUpdateDto;
import com.senla.aggregator.dto.retailer.RetailerWithAutoUpdateCardDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RetailerService {

    RetailerGetDto registerRetailer(RetailerCreateDto retailer, MultipartFile logo, UUID ownerId) throws IOException;

    RetailerGetDto getRetailerByName(String name);

    RetailerWithAutoUpdateCardDto getRetailerWithAutoUpdateInfo(UUID ownerId);

    List<RetailerGetDto> getAllRetailers(int pageNo, int pageSize);

    RetailerGetDto updateRetailer(RetailerUpdateDto retailer, MultipartFile newLogo, UUID ownerId) throws IOException;

    void deleteMyRetailer(UUID ownerId);

    void deleteRetailer(UUID retailerId);
}
