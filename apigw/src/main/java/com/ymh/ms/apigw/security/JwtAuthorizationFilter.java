package com.ymh.ms.apigw.security;

import com.ymh.ms.clients.auth.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter implements GlobalFilter, Ordered {

    private final WebClient.Builder webClientBuilder;
    private final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/logout",
            "/api/v1/auth/validate-token"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Skip validation for open endpoints
        if (isOpenEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Check for Authorization header
        if (!request.getHeaders().containsKey("Authorization")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }

        // Validate token with auth service
        return validateTokenWithAuthService(authHeader)
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
                    }
                    // Token is valid, proceed with the request
                    return chain.filter(exchange);
                });
    }

    private Mono<Boolean> validateTokenWithAuthService(String authHeader) {
        return webClientBuilder.build()
                .post()
                .uri("lb://AUTH/api/v1/auth/validate-token")
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(TokenValidationResponse.class)
                .map(TokenValidationResponse::isValid)
                .onErrorResume(e -> {
                    // Log the error
                    log.error("Error validating token: {}", e.getMessage());
                    return Mono.just(false);
                });
    }

    private boolean isOpenEndpoint(String path) {
        return openApiEndpoints.stream().anyMatch(path::equals);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
