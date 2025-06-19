package com.senla.aggregator.controller;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.retailer.RetailerCreateDto;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.dto.retailer.RetailerUpdateDto;
import com.senla.aggregator.dto.retailer.RetailerWithAutoUpdateCardDto;
import com.senla.aggregator.service.retailer.RetailerService;
import com.senla.aggregator.validation.fileTypes.AllowedFileTypes;
import com.senla.aggregator.validation.validImage.ValidImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.*;

@Validated
@RestController
@RequestMapping("api/retailers")
@RequiredArgsConstructor
@Tag(name = "Retailers Resource", description = "CRUD operations for retailer accounts")
public class RetailerController {

    private final RetailerService retailerService;

    @Operation(
            summary = "Get all retailers (admin only)",
            description = "Retrieve a paginated list of all retailers. Requires admin privileges."
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RetailerGetDto> findAllRetailers(@RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "15") int pageSize) {
        return retailerService.getAllRetailers(pageNo, pageSize);
    }

    @Operation(
            summary = "Get retailer by name",
            description = "Retrieve public info about retailer"
    )
    @GetMapping("/{retailerName}")
    public RetailerGetDto findRetailerByName(@PathVariable String retailerName) {
        return retailerService.getRetailerByName(retailerName);
    }

    @Operation(
            summary = "Get current retailer (self)",
            description = "Retrieve details of the retailer account associated with the currently authenticated user"
    )
    @GetMapping("/me")
    @PreAuthorize("hasRole('RETAILER')")
    public RetailerWithAutoUpdateCardDto findMyRetailerInfo(Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        return retailerService.getRetailerWithAutoUpdateInfo(ownerId);
    }

    @Operation(
            summary = "Create a new retailer",
            description = "Register a new retailer account. Requires retailer role",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            encoding = {
                                    @Encoding(name = "retailer", contentType = MediaType.APPLICATION_JSON_VALUE),
                                    @Encoding(name = "image", contentType = "image/png, image/jpeg")
                            }
                    )
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RETAILER')")
    @ResponseStatus(HttpStatus.CREATED)
    public RetailerGetDto createRetailer(@RequestPart(name = "image", required = false)
                                         @AllowedFileTypes(maxFileSize = 4096,
                                                 allowedFileTypes = {
                                                         ContentType.PNG,
                                                         ContentType.JPEG
                                                 }) @ValidImage MultipartFile image,
                                         @RequestPart("retailer") @Valid RetailerCreateDto retailer,
                                         Principal principal) throws IOException {
        UUID ownerId = UUID.fromString(principal.getName());
        return retailerService.registerRetailer(retailer, image, ownerId);
    }

    @Operation(
            summary = "Update current retailer",
            description = "Update the authenticated user's retailer profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            encoding = {
                                    @Encoding(name = "retailer", contentType = MediaType.APPLICATION_JSON_VALUE),
                                    @Encoding(name = "image", contentType = "image/png, image/jpeg")
                            }
                    )
            )
    )
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RETAILER')")
    public RetailerGetDto updateRetailer(@RequestPart(value = "image", required = false)
                                         @AllowedFileTypes(maxFileSize = 4096,
                                                 allowedFileTypes = {
                                                         ContentType.PNG,
                                                         ContentType.JPEG
                                                 })
                                         @ValidImage MultipartFile image,
                                         @RequestPart(name = "retailer", required = false)
                                         @Valid RetailerUpdateDto retailer,
                                         Principal principal) throws IOException {
        UUID ownerId = UUID.fromString(principal.getName());
        return retailerService.updateRetailer(retailer, image, ownerId);
    }

    @Operation(
            summary = "Delete current retailer",
            description = "Delete the currently authenticated retailer account. Requires password verification."
    )
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto deleteMyRetailer(Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        retailerService.deleteMyRetailer(ownerId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, RETAILER, ownerId))
                .build();
    }

    @Operation(
            summary = "Delete retailer by ID (admin only)",
            description = "Delete a specific retailer by their unique identifier. Requires admin privileges."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto deleteRetailer(@PathVariable UUID id) {
        retailerService.deleteRetailer(id);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, COMMENT, id))
                .build();
    }
}
