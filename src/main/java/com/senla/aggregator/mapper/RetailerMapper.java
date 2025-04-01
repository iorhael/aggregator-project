package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.retailer.RetailerCreateDto;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.dto.retailer.RetailerUpdateDto;
import com.senla.aggregator.model.Retailer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RetailerMapper {

    Retailer toRetailer(RetailerCreateDto retailer);

    @Mapping(source = "owner.username", target = "ownerName")
    RetailerGetDto toRetailerGetDto(Retailer retailer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRetailer(@MappingTarget Retailer retailer, RetailerUpdateDto dto);
}
