package com.ymh.ms.clients.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("AUTH")
public interface AuthClient {

    @PostMapping(path = "api/v1/auth/validate-token")
    TokenValidationResponse validateToken(@RequestHeader("Authorization") String authHeader);
}