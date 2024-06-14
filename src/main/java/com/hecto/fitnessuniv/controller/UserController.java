package com.hecto.fitnessuniv.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.MenteeProfileRepository;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final MenteeProfileRepository menteeProfileRepository;
    private final JwtProvider jwtProvider;

    @GetMapping("/check-user-role")
    public ResponseEntity<String> checkUserRole(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userId);
                if (userEntityOptional.isPresent()) {
                    UserEntity userEntity = userEntityOptional.get();
                    return ResponseEntity.ok(userEntity.getRole());
                } else {
                    return ResponseEntity.ok("ROLE_NEW");
                }
            } else {
                return ResponseEntity.status(401).body("Invalid token");
            }
        } else {
            return ResponseEntity.status(400).body("Bearer token is missing or invalid");
        }
    }

    @GetMapping("/get-user-id")
    public ResponseEntity<String> getUserId(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                return ResponseEntity.ok(userId);
            } else {
                return ResponseEntity.status(401).body("Invalid token");
            }
        } else {
            return ResponseEntity.status(400).body("Bearer token is missing or invalid");
        }
    }

    @GetMapping("/get-user-name")
    public ResponseEntity<String> getUserName(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userId);
                if (userEntityOptional.isPresent()) {
                    UserEntity userEntity = userEntityOptional.get();
                    return ResponseEntity.ok(userEntity.getUserName());
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
