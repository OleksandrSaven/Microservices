package com.gatewayservice.security;

import com.gatewayservice.exeption.CustomJwtException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
public class AuthenticationFilter extends
        AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    public static final List<String> ADMIN_BOOK_ENDPOINTS = List.of(
            "/api/books",
            "/api/category"
    );
    public static final List<String> ADMIN_ORDER_ENDPOINTS = List.of(
            "/api/orders"
    );
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ADMIN = "ADMIN";

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            HttpMethod method = exchange.getRequest().getMethod();
            if (routeValidator.getSecuredEndpoints().test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new CustomJwtException("Missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders()
                        .get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                    authHeader = authHeader.substring(BEARER_PREFIX.length());
                }
                try {
                    jwtUtil.isValidToken(authHeader);
                } catch (RestClientException e) {
                    throw new CustomJwtException("Un authorize access to application");
                }
                String userIdentifier = jwtUtil.getUserName(authHeader);
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Email", userIdentifier)
                        .build();
                exchange = exchange.mutate().request(modifiedRequest).build();

                if (!jwtUtil.hasAnyRole(authHeader, config.getRole())) {
                    var response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                String path = subPath(exchange.getRequest().getPath().value());
                if (ADMIN_BOOK_ENDPOINTS.contains(path)) {
                    if (isProtectedMethod(method) && !jwtUtil.hasAnyRole(
                            authHeader, List.of(ADMIN))) {
                        var response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        return response.setComplete();
                    }
                }
                if (ADMIN_ORDER_ENDPOINTS.contains(path)) {
                    if (method.equals(HttpMethod.PATCH) && !jwtUtil.hasAnyRole(
                            authHeader, List.of(ADMIN))) {
                        var response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        return response.setComplete();
                    }
                }
            }
            return chain.filter(exchange);
        });
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("role");
    }

    @Setter
    @Getter
    public static class Config {
        private List<String> role;
    }

    private boolean isProtectedMethod(HttpMethod method) {
        return method == HttpMethod.POST
                || method == HttpMethod.PUT
                || method == HttpMethod.PATCH
                || method == HttpMethod.DELETE;
    }

    private String subPath(String path) {
        int secondSlashIndex = path.indexOf("/", path.indexOf("/") + 1);
        int thirdSlashIndex = path.indexOf("/", secondSlashIndex + 1);
        if (thirdSlashIndex != -1) {
            return path.substring(0, thirdSlashIndex);
        }
        return path;
    }
}
