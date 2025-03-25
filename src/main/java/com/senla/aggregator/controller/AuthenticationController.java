package com.senla.aggregator.controller;

import com.senla.aggregator.dto.auth.LoginDto;
import com.senla.aggregator.dto.auth.RegisterDto;
import com.senla.aggregator.dto.auth.TokenDto;
import com.senla.aggregator.dto.user.UserProfileDto;
import com.senla.aggregator.service.keycloak.KeycloakService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Resource", description = "Login/Sign Up actions")
public class AuthenticationController {

    private final KeycloakService keycloakService;

    @PostMapping("login")
    public TokenDto login(@Valid @RequestBody LoginDto loginDto) {
        return keycloakService.getAccessToken(loginDto);
    }

    @PostMapping("signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileDto signup(@Valid @RequestBody RegisterDto registerDto) {
        return keycloakService.registerUser(registerDto);
    }
}
