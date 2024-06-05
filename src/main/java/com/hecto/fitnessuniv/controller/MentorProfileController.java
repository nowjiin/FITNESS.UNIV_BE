package com.hecto.fitnessuniv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.MentorProfileEntity;
import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;
import com.hecto.fitnessuniv.repository.UserRepository;
import com.hecto.fitnessuniv.service.MentorProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MentorProfileController {
    private final MentorProfileRepository mentorProfileRepository;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final MentorProfileService service;

    @PostMapping("/mentor")
    public ResponseEntity<MentorProfileEntity> createMentorProfile(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody MentorProfileEntity mentorProfileEntity) {

        // Bearer 토큰에서 JWT 추출
        String token = bearerToken.substring(7);
        // JWT에서 사용자 ID 추출
        String userId = jwtProvider.getUserIdFromToken(token);
        // UserEntity 찾기
        UserEntity user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        // MentorProfile에 User 설정
        mentorProfileEntity.setUser(user);

        MentorProfileEntity savedMentorProfileEntity =
                service.saveMentorProfile(mentorProfileEntity);
        return new ResponseEntity<>(savedMentorProfileEntity, HttpStatus.CREATED);
    }
}
