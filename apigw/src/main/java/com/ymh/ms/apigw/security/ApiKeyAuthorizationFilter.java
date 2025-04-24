package com.ymh.ms.apigw.security;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

//@Component
@AllArgsConstructor
public class ApiKeyAuthorizationFilter implements GlobalFilter, Ordered {

    private final FakeApiAuthorizationChecker fakeApiAuthorizationChecker;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        List<String> apiKeys = exchange.getRequest().getHeaders().get("API-KEY");
        String application = route.getId();


        if (application == null ||
                (apiKeys == null || apiKeys.isEmpty() || !fakeApiAuthorizationChecker.isAuthorized(apiKeys.get(0), application))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized.");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
