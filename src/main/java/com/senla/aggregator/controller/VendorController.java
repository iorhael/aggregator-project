package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.vendor.VendorCreateDto;
import com.senla.aggregator.dto.vendor.VendorGetDto;
import com.senla.aggregator.service.vendor.VendorService;
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

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Messages.DELETION_MESSAGE;
import static com.senla.aggregator.controller.helper.Messages.VENDOR;

@RestController
@RequestMapping("api/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendors Resource", description = "CRUD")
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("/{id}")
    public VendorGetDto findVendorById(@PathVariable UUID id) {
        return vendorService.getVendor(id);
    }

    @GetMapping
    public List<VendorGetDto> findAllVendors(@RequestParam(defaultValue = "0") int pageNo,
                                             @RequestParam(defaultValue = "15") int pageSize) {
        return vendorService.getAllVendors(pageNo, pageSize);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorGetDto createVendor(@Valid @RequestBody VendorCreateDto vendor) {
        return vendorService.createVendor(vendor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VendorGetDto updateVendor(@RequestBody VendorCreateDto vendor,
                                     @PathVariable UUID id) {
        return vendorService.updateVendor(vendor, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto deleteVendor(@PathVariable UUID id) {
        vendorService.deleteVendor(id);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, VENDOR, id))
                .build();
    }
}
