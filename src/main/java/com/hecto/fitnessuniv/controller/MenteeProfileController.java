package com.hecto.fitnessuniv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.MenteeProfileEntity;
import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;
import com.hecto.fitnessuniv.service.MenteeProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenteeProfileController {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final MenteeProfileService service;

    @PostMapping("/mentee")
    public ResponseEntity<MenteeProfileEntity> createMenteeProfile(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody MenteeProfileEntity menteeProfileEntity) {

        // Bearer 토큰에서 JWT 추출
        String token = bearerToken.substring(7);
        // JWT에서 사용자 ID 추출
        String userId = jwtProvider.getUserIdFromToken(token);
        // UserEntity 찾기
        UserEntity user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        // MenteeProfile에 User 설정
        menteeProfileEntity.setUser(user);

        MenteeProfileEntity savedMenteeProfileEntity =
                service.saveMenteeProfile(menteeProfileEntity);
        return new ResponseEntity<>(savedMenteeProfileEntity, HttpStatus.CREATED);
    }
}
