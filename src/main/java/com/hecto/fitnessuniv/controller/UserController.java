package com.hecto.fitnessuniv.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired private UserRepository userRepository;

    @Autowired private JwtProvider jwtProvider;

    @GetMapping("/current")
    public ResponseEntity<UserEntity> getCurrentUser(
            @RequestHeader("Authorization") String authorizationHeader) {
        // "Bearer " 부분을 제거하여 실제 JWT를 가져옵니다.
        String token = authorizationHeader.substring(7);
        // JWT를 검증하고 사용자 ID를 추출합니다.
        if (jwtProvider.validate(token)) {
            String userId = jwtProvider.getUserIdFromToken(token);
            Optional<UserEntity> user = userRepository.findByUserId(userId);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }
}
