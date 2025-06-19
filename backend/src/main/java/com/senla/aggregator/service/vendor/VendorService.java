package com.senla.aggregator.service.vendor;

import com.senla.aggregator.dto.vendor.VendorCreateDto;
import com.senla.aggregator.dto.vendor.VendorGetDto;

import java.util.List;
import java.util.UUID;

public interface VendorService {

    VendorGetDto getVendor(UUID id);

    VendorGetDto createVendor(VendorCreateDto vendor);

    List<VendorGetDto> getAllVendors(int pageNo, int pageSize);

    VendorGetDto updateVendor(VendorCreateDto vendor, UUID id);

    void deleteVendor(UUID id);
}
