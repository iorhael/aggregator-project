package com.senla.aggregator.service.productCard;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.JobInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductCardBatchService {

    Long importProductCards(MultipartFile file, UUID retailerOwnerId, Boolean verifiedProductsOnly) throws IOException;

    Long updateProductCards(MultipartFile file, UUID retailerOwnerId) throws IOException;

    Long exportProductCards(UUID retailerOwnerId, ContentType type) throws IOException;

    List<JobInfoDto> getExecutionsHistory(UUID userId, int pageNo, int pageSize);
}
