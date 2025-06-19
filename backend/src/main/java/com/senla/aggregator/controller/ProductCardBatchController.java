package com.senla.aggregator.controller;

import com.senla.aggregator.controller.helper.Constants;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.AutoUpdateCardDto;
import com.senla.aggregator.dto.JobInfoDto;
import com.senla.aggregator.dto.OnCreateGroup;
import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.service.productCard.ProductCardBatchService;
import com.senla.aggregator.validation.contentTypes.AllowedContentTypes;
import com.senla.aggregator.validation.fileTypes.AllowedFileTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("api/product_cards_batch")
@RequiredArgsConstructor
@Tag(name = "Product Cards Batch Resource", description = "Batch operations")
public class ProductCardBatchController {

    private final ProductCardBatchService productCardBatchService;

    @Operation(
            summary = "Import product cards",
            description = "Start batch import of product cards from a CSV, JSON or XML file. Returns job execution ID."
    )
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto importProductCards(@RequestPart("file")
                                              @AllowedFileTypes(allowedFileTypes = {
                                                      ContentType.CSV,
                                                      ContentType.XML,
                                                      ContentType.JSON
                                              }) MultipartFile file,
                                              @RequestParam(defaultValue = "true") Boolean verifiedProductsOnly,
                                              Principal principal) throws IOException {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        Long executionId = productCardBatchService.importProductCards(file, retailerOwnerId, verifiedProductsOnly);

        return ResponseInfoDto.builder()
                .message(String.format(Constants.JOB_SUCCESSFULLY_STARTED, executionId))
                .build();
    }

    @Operation(
            summary = "Enable auto update for product cards",
            description = "Allow daily scheduled task execution. The task replaces the offers in the database with those specified in the file at the link"
    )
    @PostMapping("/auto_update")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto enableAutoUpdate(@Validated(OnCreateGroup.class) @RequestBody AutoUpdateCardDto dto,
                                            Principal principal) {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        productCardBatchService.enableAutoUpdate(dto, retailerOwnerId);

        return ResponseInfoDto.builder()
                .message(Constants.AUTO_UPDATE_RULE_ADD_MESSAGE)
                .build();
    }

    @Operation(
            summary = "Disable auto update for product cards",
            description = "Disable daily scheduled task execution"
    )
    @DeleteMapping("/auto_update")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto disableAutoUpdate(Principal principal) {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        productCardBatchService.disableAutoUpdate(retailerOwnerId);

        return ResponseInfoDto.builder()
                .message(Constants.AUTO_UPDATE_RULE_DISABLE_MESSAGE)
                .build();
    }

    @Operation(
            summary = "Change auto update rules for product cards",
            description = "Update file download link or verifiedProductOnly policy"
    )
    @PutMapping("/auto_update")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto changeAutoUpdateRules(@Valid @RequestBody AutoUpdateCardDto dto,
                                                 Principal principal) {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        productCardBatchService.changeAutoUpdateRules(dto, retailerOwnerId);

        return ResponseInfoDto.builder()
                .message(Constants.AUTO_UPDATE_RULE_UPDATE_MESSAGE)
                .build();
    }

    @Operation(
            summary = "Update product cards",
            description = "Start batch update of product cards from a CSV, JSON or XML file. Returns job execution ID."
    )
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto updateProductCards(@RequestPart("file")
                                              @AllowedFileTypes(allowedFileTypes = {
                                                      ContentType.CSV,
                                                      ContentType.XML,
                                                      ContentType.JSON
                                              }) MultipartFile file,
                                              Principal principal) throws IOException {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        Long executionId = productCardBatchService.updateProductCards(file, retailerOwnerId);

        return ResponseInfoDto.builder()
                .message(String.format(Constants.JOB_SUCCESSFULLY_STARTED, executionId))
                .build();
    }

    @Operation(
            summary = "Export product cards",
            description = "Export all product cards of the current retailer in the specified format (CSV, JSON, XML)."
    )
    @GetMapping("/export")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto exportProductCards(@RequestParam
                                              @AllowedContentTypes(allowedContentTypes = {
                                                      ContentType.JSON,
                                                      ContentType.XML,
                                                      ContentType.CSV
                                              }) ContentType type,
                                              Principal principal) throws IOException {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        Long executionId = productCardBatchService.exportProductCards(retailerOwnerId, type);

        return ResponseInfoDto.builder()
                .message(String.format(Constants.JOB_SUCCESSFULLY_STARTED, executionId))
                .build();
    }

    @Operation(
            summary = "Get job execution history",
            description = "Retrieve paginated list of batch import/update jobs for the current retailer."
    )
    @GetMapping("/job_executions")
    @PreAuthorize("hasRole('RETAILER')")
    public PaginationStatsDto<JobInfoDto> getJobHistory(@RequestParam(defaultValue = "0") int pageNo,
                                                        @RequestParam(defaultValue = "15") int pageSize,
                                                        Principal principal) {
        UUID retailerOwnerId = UUID.fromString(principal.getName());
        return productCardBatchService.getExecutionsHistory(retailerOwnerId, pageNo, pageSize);
    }
}
