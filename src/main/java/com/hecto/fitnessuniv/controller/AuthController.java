package com.hecto.fitnessuniv.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAuthToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String refreshToken = bearerToken.substring(7);
            if (jwtProvider.validate(refreshToken)) {
                String userId = jwtProvider.getUserIdFromToken(refreshToken);
                Optional<UserEntity> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    UserEntity user = userOptional.get();
                    if (refreshToken.equals(user.getRefreshToken())) {
                        String newAccessToken = jwtProvider.refreshAccessToken(refreshToken);
                        return ResponseEntity.ok(Map.of("token", newAccessToken));
                    } else {
                        return ResponseEntity.status(401).body("Invalid refresh token");
                    }
                } else {
                    return ResponseEntity.status(404).body("User not found");
                }
            } else {
                return ResponseEntity.status(401).body("Invalid token");
            }
        } else {
            return ResponseEntity.status(400).body("Bearer token is missing or invalid");
        }
    }
}
