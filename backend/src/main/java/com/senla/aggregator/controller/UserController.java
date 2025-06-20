package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.user.UserDto;
import com.senla.aggregator.dto.user.UserProfileDto;
import com.senla.aggregator.dto.user.UserPromotionDto;
import com.senla.aggregator.dto.user.UserUpdateDto;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.model.User;
import com.senla.aggregator.service.keycloak.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Tag(name = "Users Resource", description = "Manage users")
public class UserController {

    private final KeycloakService keycloakService;

    @Operation(
            summary = "Get current user profile",
            description = "Retrieve profile information of the currently authenticated user"
    )
    @GetMapping
    public UserProfileDto getProfileInfo(Principal principal) {
        return keycloakService.getUser(principal.getName());
    }

    @Operation(
            summary = "Update user profile",
            description = "Update the profile information of the authenticated user"
    )
    @PutMapping
    public UserProfileDto updateProfileInfo(@Valid @RequestBody UserUpdateDto dto,
                                            Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return keycloakService.updateUser(dto, userId);
    }

    @PostMapping("/synchronization")
    public ResponseInfoDto synchronizeUser(JwtAuthenticationToken token) {
        Jwt jwtToken = token.getToken();

        String username = jwtToken.getClaim("preffered_username");
        String email = jwtToken.getClaim("email");
        String roleString = extractClientRole(jwtToken, "aggregator-realm");
        Role role = Role.valueOf(roleString);

        keycloakService.synchronizeUser(new UserDto(username, email, role));

        return ResponseInfoDto.builder()
                .message("Successfully synchronized user")
                .build();
    }

    @Operation(
            summary = "Update password",
            description = "Update the password of the authenticated user"
    )
    @PutMapping("/password")
    public ResponseInfoDto updatePassword(Principal principal) {
        String userId = principal.getName();
        String newPassword = "delete it";
        keycloakService.updatePassword(userId, newPassword);

        return ResponseInfoDto.builder()
                .message(PASSWORD_UPDATED_MESSAGE)
                .build();
    }

    @Operation(
            summary = "Delete user account",
            description = "Delete the authenticated user's account"
    )
    @DeleteMapping
    public ResponseInfoDto deleteUser(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        keycloakService.deleteUser(userId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, USER, userId))
                .build();
    }

    @Operation(
            summary = "Promote user",
            description = "Assign a new role to a user (admin only)"
    )
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

    private String extractClientRole(Jwt jwt, String clientId) {
        Map<String, Map<String, List<String>>> resourceAccess = jwt.getClaim("resource_access");
        if (Objects.nonNull(resourceAccess) && resourceAccess.containsKey(clientId)) {
            List<String> roles = resourceAccess.get(clientId).get("roles");
            if (Objects.nonNull(roles) && !roles.isEmpty()) {
                return roles.getFirst();
            }
        }
        return null;
    }
}
