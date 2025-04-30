package com.ymh.ms.auth.controller;

import com.ymh.ms.auth.dto.AuthenticationRequest;
import com.ymh.ms.auth.dto.AuthenticationResponse;
import com.ymh.ms.auth.dto.RegisterRequest;
import com.ymh.ms.auth.service.AuthenticationService;
import com.ymh.ms.clients.auth.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String refreshToken = authHeader.substring(7);
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Since JWT is stateless, we don't need to do anything on the server side for logout
        // The client should simply remove the token from its storage
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(TokenValidationResponse.builder().valid(false).message("Invalid authorization header").build());
        }

        String token = authHeader.substring(7);
        boolean isValid = authenticationService.validateToken(token);

        if (isValid) {
            String username = authenticationService.extractUsernameFromToken(token);
            return ResponseEntity.ok(TokenValidationResponse.builder().valid(true).username(username).build());
        } else {
            return ResponseEntity.ok(TokenValidationResponse.builder().valid(false).message("Invalid or expired token").build());
        }
    }
}
