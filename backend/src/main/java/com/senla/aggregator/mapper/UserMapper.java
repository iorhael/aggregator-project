package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.auth.RegisterDto;
import com.senla.aggregator.dto.user.UserDto;
import com.senla.aggregator.dto.user.UserProfileDto;
import com.senla.aggregator.dto.user.UserUpdateDto;
import com.senla.aggregator.model.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRepresentation user);

    UserProfileDto toUserProfileDto(UserRepresentation user);

    UserRepresentation toUserRepresentation(RegisterDto user);

    UserRepresentation toUserRepresentation(UserUpdateDto user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User oldUser, UserDto newUser);
}
