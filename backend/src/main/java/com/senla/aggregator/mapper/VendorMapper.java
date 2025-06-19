package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.vendor.VendorCreateDto;
import com.senla.aggregator.dto.vendor.VendorGetDto;
import com.senla.aggregator.model.Vendor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    VendorGetDto toVendorGetDto(Vendor vendor);

    Vendor toVendor(VendorCreateDto vendor);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVendor(@MappingTarget Vendor vendor, VendorCreateDto dto);
}
