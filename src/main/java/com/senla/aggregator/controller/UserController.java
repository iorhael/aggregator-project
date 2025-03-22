package com.senla.aggregator.controller;

import com.senla.aggregator.aspect.VerifyPassword;
import com.senla.aggregator.dto.PasswordDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.user.UserPromotionDto;
import com.senla.aggregator.dto.user.PasswordUpdateDto;
import com.senla.aggregator.dto.user.UserProfileDto;
import com.senla.aggregator.dto.user.UserUpdateDto;
import com.senla.aggregator.service.keycloak.KeycloakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static com.senla.aggregator.controller.ControllerMessages.DELETION_MESSAGE;
import static com.senla.aggregator.controller.ControllerMessages.PROMOTION_MESSAGE;
import static com.senla.aggregator.controller.ControllerMessages.PASSWORD_UPDATED_MESSAGE;
import static com.senla.aggregator.controller.ControllerMessages.USER;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakService keycloakService;

    @GetMapping
    public UserProfileDto getProfileInfo(Principal principal) {
        return keycloakService.getUser(principal.getName());
    }

    @PutMapping
    public UserProfileDto updateProfileInfo(@Valid @RequestBody UserUpdateDto dto,
                                            Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        return keycloakService.updateUser(dto, userId);
    }

    @PutMapping("/password")
    @VerifyPassword(password = "#dto.oldPassword")
    public ResponseInfoDto updatePassword(@Valid @RequestBody PasswordUpdateDto dto,
                                          Principal principal) {
        String userId = principal.getName();
        String newPassword = dto.getNewPassword();

        keycloakService.updatePassword(userId, newPassword);

        return ResponseInfoDto.builder()
                .message(PASSWORD_UPDATED_MESSAGE)
                .build();
    }

    @DeleteMapping
    @VerifyPassword(password = "#dto.password")
    public ResponseInfoDto deleteUser(@Valid @RequestBody PasswordDto dto,
                                      Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        keycloakService.deleteUser(userId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, USER, userId))
                .build();
    }

    @PutMapping("/promotion/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto promoteUser(@Valid @RequestBody UserPromotionDto dto,
                                       @PathVariable UUID id) {
        String roleName = dto.getRole().name();

        keycloakService.addClientRoleToUser(id, roleName);

        return ResponseInfoDto.builder()
                .message(String.format(PROMOTION_MESSAGE, id))
                .build();
    }
}
