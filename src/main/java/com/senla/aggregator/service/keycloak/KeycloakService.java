package com.senla.aggregator.service.keycloak;

import com.senla.aggregator.dto.auth.LoginDto;
import com.senla.aggregator.dto.auth.RegisterDto;
import com.senla.aggregator.dto.auth.TokenDto;
import com.senla.aggregator.dto.user.UserProfileDto;
import com.senla.aggregator.dto.user.UserUpdateDto;

import java.util.UUID;

public interface KeycloakService {

    TokenDto getAccessToken(LoginDto dto);

    UserProfileDto registerUser(RegisterDto dto);

    UserProfileDto getUser(String userId);

    UserProfileDto updateUser(UserUpdateDto dto, UUID userId);

    void deleteUser(UUID userId);

    void addClientRoleToUser(UUID userId, String roleName);

    void updatePassword(String userId, String newPassword);

    void verifyPassword(String userId, String oldPassword);
}
