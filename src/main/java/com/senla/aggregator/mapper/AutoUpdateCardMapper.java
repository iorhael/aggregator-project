package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.AutoUpdateCardDto;
import com.senla.aggregator.model.AutoUpdateCard;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AutoUpdateCardMapper {

    AutoUpdateCard toAutoUpdateCard(AutoUpdateCardDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAutoUpdateCard(@MappingTarget AutoUpdateCard info, AutoUpdateCardDto dto);
}
