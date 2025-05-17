package com.senla.aggregator.service.keycloak;

import com.senla.aggregator.model.credential.KeycloakCredentials;
import com.senla.aggregator.dto.auth.LoginDto;
import com.senla.aggregator.dto.auth.RegisterDto;
import com.senla.aggregator.dto.auth.TokenDto;
import com.senla.aggregator.dto.user.UserProfileDto;
import com.senla.aggregator.dto.user.UserUpdateDto;
import com.senla.aggregator.mapper.UserMapper;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.model.User;
import com.senla.aggregator.repository.UserRepository;
import com.senla.aggregator.service.exception.ExceptionMessages;
import com.senla.aggregator.service.exception.KeycloakException;
import com.senla.aggregator.service.mail.GmailApiService;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak adminKeycloak;

    private final KeycloakCredentials keycloakCredentials;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public TokenDto getAccessToken(LoginDto dto) {
        try (Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakCredentials.serverUrl())
                .realm(keycloakCredentials.realm())
                .clientId(keycloakCredentials.clientId())
                .clientSecret(keycloakCredentials.clientSecret())
                .grantType(OAuth2Constants.PASSWORD)
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build()) {

            return new TokenDto(keycloak.tokenManager().getAccessTokenString());
        } catch (NotAuthorizedException e) {
            throw new KeycloakException(ExceptionMessages.KEYCLOAK_BAD_CREDENTIALS, e);
        }
    }

    @Override
    public UserProfileDto registerUser(RegisterDto dto) {
        UserRepresentation user = userMapper.toUserRepresentation(dto);
        CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());

        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credential));

        UUID userId = createKeycloakUser(user);
        addClientRoleToUser(userId, Role.DEFAULT.name());

        User dbUser = userMapper.toUser(user);
        dbUser.setRole(Role.DEFAULT);
        dbUser.setId(userId);

        userRepository.save(dbUser);

        return userMapper.toUserProfileDto(user);
    }

    @Override
    public UserProfileDto getUser(String userId) {
        return userMapper.toUserProfileDto(
                adminKeycloak.realm(keycloakCredentials.realm())
                        .users()
                        .get(userId)
                        .toRepresentation()
        );
    }

    @Override
    public UserProfileDto updateUser(UserUpdateDto dto, UUID userId) {
        UserRepresentation user = userMapper.toUserRepresentation(dto);

        UserRepresentation updatedUser = updateKeycloakUser(user, userId);

        if (Objects.nonNull(dto.getEmail()))
            userRepository.save(userMapper.toUser(updatedUser));

        return userMapper.toUserProfileDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        getUsersResource().delete(userId.toString()).close();

        userRepository.deleteById(userId);
    }

    @Override
    public void updatePassword(String userId, String newPassword) {
        CredentialRepresentation credential = createPasswordCredentials(newPassword);

        getUsersResource().get(userId)
                .resetPassword(credential);
    }

    @Override
    public void verifyPassword(String userId, String password) {
        String username = getUsersResource().get(userId)
                .toRepresentation()
                .getUsername();

        getAccessToken(new LoginDto(username, password));
    }

    @Override
    public void addClientRoleToUser(UUID userId, String roleName) {
        RealmResource realmResource = adminKeycloak.realm(keycloakCredentials.realm());

        RoleRepresentation role = realmResource.clients()
                .get(keycloakCredentials.clientDatabaseId())
                .roles()
                .get(roleName)
                .toRepresentation();

        RoleMappingResource roleMappingResource = realmResource.users()
                .get(userId.toString())
                .roles();

        roleMappingResource.clientLevel(keycloakCredentials.clientDatabaseId())
                .add(Collections.singletonList(role));
    }

    private UsersResource getUsersResource() {
        return adminKeycloak.realm(keycloakCredentials.realm()).users();
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);

        return passwordCredentials;
    }

    private UUID extractUserIdFromResponse(Response response) {
        String location = response.getLocation().toString();

        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID createKeycloakUser(UserRepresentation user) {
        UsersResource usersResource = getUsersResource();

        try (Response response = usersResource.create(user)) {
            if (response.getStatusInfo().getFamily() == Response.Status.Family.CLIENT_ERROR)
                throw new KeycloakException(ExceptionMessages.KEYCLOAK_CONFLICT);

            return extractUserIdFromResponse(response);
        }
    }

    private UserRepresentation updateKeycloakUser(UserRepresentation user, UUID userId) {
        UserResource userResource = getUsersResource().get(userId.toString());

        userResource.update(user);

        return userResource.toRepresentation();
    }
}
