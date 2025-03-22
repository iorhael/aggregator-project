package com.senla.aggregator.aspect;

import com.senla.aggregator.service.keycloak.KeycloakService;
import com.senla.aggregator.util.CustomSpringExpressionLanguageParser;
import com.senla.aggregator.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class PasswordVerifierAspect {

    private final KeycloakService keycloakService;

    @Before("@annotation(VerifyPassword)")
    public void verifyPassword(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        VerifyPassword verifyPasswordAnnotation = method.getAnnotation(VerifyPassword.class);

        String password = (String) CustomSpringExpressionLanguageParser.getDynamicValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                verifyPasswordAnnotation.password()
        );

        keycloakService.verifyPassword(SecurityUtil.getPrincipalName(), password);
    }
}
