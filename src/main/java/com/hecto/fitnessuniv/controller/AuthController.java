package com.hecto.fitnessuniv.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final JwtProvider jwtProvider;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAuthToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            try {
                String token = bearerToken.substring(7);
                if (jwtProvider.validate(token)) {
//                    String newToken = jwtProvider.refreshToken(token);
//                    return ResponseEntity.ok(Map.of("token", newToken));
                } else {
                    return ResponseEntity.status(401).body("Invalid token");
                }
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Token refresh failed");
            }
        } else {
            return ResponseEntity.status(400).body("Bearer token is missing or invalid");
        }
        return null;
    }
}
