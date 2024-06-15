package com.hecto.fitnessuniv.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAuthToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body("Bearer token is missing or invalid");
        }

        String refreshToken = bearerToken.substring(7);
        if (!jwtProvider.validate(refreshToken)) {
            log.info("Invalid refresh token");
            return ResponseEntity.status(401).body("Invalid token");
        }

        String userId = jwtProvider.getUserIdFromToken(refreshToken);
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            log.info("User not found");
            return ResponseEntity.status(404).body("User not found");
        }

        UserEntity user = userOptional.get();
        if (!refreshToken.equals(user.getRefreshToken())) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        String newAccessToken = jwtProvider.refreshAccessToken(refreshToken);
        log.info("AccessToken 재발급 for user: {}", userId);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
