package com.ymh.ms.auth.service;

import com.ymh.ms.auth.dto.AuthenticationRequest;
import com.ymh.ms.auth.dto.AuthenticationResponse;
import com.ymh.ms.auth.dto.RegisterRequest;
import com.ymh.ms.auth.model.User;
import com.ymh.ms.auth.repository.UserRepository;
import com.ymh.ms.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        return jwtService.createAuthenticationResponse(userDetails);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        user.setLastLoginDttm(LocalDateTime.now());
        userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        return jwtService.createAuthenticationResponse(userDetails);
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            return jwtService.refreshAccessToken(refreshToken, userDetails);
        }
        throw new IllegalArgumentException("Invalid refresh token");
    }

    public boolean validateToken(String token) {
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
        return jwtService.isTokenValid(token, userDetails);
    }

    public String extractUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
    }
}
