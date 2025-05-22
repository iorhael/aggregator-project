package com.senla.aggregator.controller;

import com.senla.aggregator.aspect.VerifyPassword;
import com.senla.aggregator.dto.PasswordDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.retailer.RetailerCreateDto;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.dto.retailer.RetailerUpdateDto;
import com.senla.aggregator.service.retailer.RetailerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.*;

@RestController
@RequestMapping("api/retailers")
@RequiredArgsConstructor
@Tag(name = "Retailers Resource", description = "CRUD")
public class RetailerController {

    private final RetailerService retailerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RetailerGetDto> findAllRetailers(@RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "15") int pageSize) {
        return retailerService.getAllRetailers(pageNo, pageSize);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('RETAILER')")
    public RetailerGetDto findRetailer(Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return retailerService.getRetailerByOwnerId(ownerId);
    }

    @PostMapping
    @PreAuthorize("hasRole('RETAILER')")
    @ResponseStatus(HttpStatus.CREATED)
    public RetailerGetDto createRetailer(@Valid @RequestBody RetailerCreateDto retailer,
                                         Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return retailerService.registerRetailer(retailer, ownerId);
    }

    @PutMapping
    @PreAuthorize("hasRole('RETAILER')")
    public RetailerGetDto updateRetailer(@Valid @RequestBody RetailerUpdateDto retailer,
                                         Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return retailerService.updateRetailer(retailer, ownerId);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('RETAILER')")
    @VerifyPassword(password = "#dto.password")
    public ResponseInfoDto deleteMyRetailer(@Valid @RequestBody PasswordDto dto,
                                            Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        retailerService.checkOwnershipAndDeleteRetailer(ownerId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, RETAILER, ownerId))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto deleteRetailer(@PathVariable UUID id) {
        retailerService.deleteRetailer(id);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, COMMENT, id))
                .build();
    }
}
