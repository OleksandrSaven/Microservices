package com.gatewayservice.security;

import java.util.List;
import java.util.function.Predicate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {
    public static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/auth/register",
            "/auth/login",
            "/eureka",
            "/v3/api-docs",
            "/swagger-ui"
    );

    private Predicate<ServerHttpRequest> securedEndpoints =
            serverHttpRequest -> OPEN_API_ENDPOINTS.stream()
                    .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));

    public Predicate<ServerHttpRequest> getSecuredEndpoints() {
        return securedEndpoints;
    }
}
