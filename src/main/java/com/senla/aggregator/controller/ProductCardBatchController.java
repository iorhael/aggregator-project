package com.senla.aggregator.controller;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.controller.helper.Messages;
import com.senla.aggregator.dto.JobInfoDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.service.productCard.ProductCardBatchService;
import com.senla.aggregator.validation.AllowedFileTypes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("api/product_cards_batch")
@RequiredArgsConstructor
@Tag(name = "Product Cards Batch Resource", description = "Batch operations")
public class ProductCardBatchController {

    private final ProductCardBatchService productCardBatchService;

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto importProductCards(@RequestPart("file")
                                              @AllowedFileTypes(allowedFileTypes = {
                                                      ContentType.CSV,
                                                      ContentType.XML,
                                                      ContentType.JSON
                                              }) MultipartFile file,
                                              @RequestParam(defaultValue = "true") Boolean verifiedProductsOnly,
                                              Principal principal) throws Exception {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        Long executionId = productCardBatchService.importProductCards(file, retailerOwnerId, verifiedProductsOnly);

        return ResponseInfoDto.builder()
                .message(String.format(Messages.JOB_SUCCESSFULLY_STARTED, executionId))
                .build();
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto updateProductCards(@RequestPart("file")
                                              @AllowedFileTypes(allowedFileTypes = {
                                                      ContentType.CSV,
                                                      ContentType.XML,
                                                      ContentType.JSON
                                              }) MultipartFile file,
                                              Principal principal) throws Exception {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        Long executionId = productCardBatchService.updateProductCards(file, retailerOwnerId);

        return ResponseInfoDto.builder()
                .message(String.format(Messages.JOB_SUCCESSFULLY_STARTED, executionId))
                .build();
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseEntity<Resource> exportProductCards(@RequestParam ContentType type,
                                                       Principal principal) throws Exception {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        Path exportedFilePath = productCardBatchService.exportProductCards(retailerOwnerId, type);

        FileSystemResource resource = new FileSystemResource(exportedFilePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(type.getValue()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/job_executions")
    @PreAuthorize("hasRole('RETAILER')")
    public List<JobInfoDto> get(@RequestParam(defaultValue = "0") int pageNo,
                                @RequestParam(defaultValue = "15") int pageSize,
                                Principal principal) {
        UUID retailerOwnerId = UUID.fromString(principal.getName());

        return productCardBatchService.getExecutionsHistory(retailerOwnerId, pageNo, pageSize);
    }
}
