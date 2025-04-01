package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.store.StoreCreateDto;
import com.senla.aggregator.dto.store.StoreGetDto;
import com.senla.aggregator.dto.store.StoreUpdateDto;
import com.senla.aggregator.model.Store;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    Store toStore(StoreCreateDto store);

    @Mapping(source = "retailer.name", target = "retailerName")
    StoreGetDto toStoreGetDto(Store store);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStore(@MappingTarget Store store, StoreUpdateDto dto);
}
