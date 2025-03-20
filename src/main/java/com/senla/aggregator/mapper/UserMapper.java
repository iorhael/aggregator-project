package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.auth.RegisterDto;
import com.senla.aggregator.dto.user.UserProfileDto;
import com.senla.aggregator.dto.user.UserUpdateDto;
import com.senla.aggregator.model.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRepresentation user);

    UserProfileDto toUserProfileDto(UserRepresentation user);

    UserRepresentation toUserRepresentation(RegisterDto user);

    UserRepresentation toUserRepresentation(UserUpdateDto user);
}
