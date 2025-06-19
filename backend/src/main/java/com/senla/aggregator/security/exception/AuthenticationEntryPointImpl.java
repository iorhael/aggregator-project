package com.senla.aggregator.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.ResponseInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ResponseInfoDto error = ResponseInfoDto.builder()
                .message(authException.getMessage())
                .build();

        response.setContentType(ContentType.JSON.getValue());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
