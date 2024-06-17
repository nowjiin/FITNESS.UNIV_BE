package com.hecto.fitnessuniv.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.MenteeProfileEntity;
import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.MenteeProfileRepository;
import com.hecto.fitnessuniv.repository.UserRepository;
import com.hecto.fitnessuniv.service.MenteeProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenteeProfileController {
    private final MenteeProfileRepository menteeProfileRepository;
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

    @GetMapping("/mentee")
    public ResponseEntity<List<MenteeProfileEntity>> getAllMenteesProfile() {
        List<MenteeProfileEntity> mentees = service.getAllMenteesProfile();
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    @GetMapping("/mentee/{id}")
    public ResponseEntity<MenteeProfileEntity> getMenteeById(@PathVariable Long id) {
        Optional<MenteeProfileEntity> menteeProfile = menteeProfileRepository.findById(id);
        if (menteeProfile.isPresent()) {
            return new ResponseEntity<>(menteeProfile.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mentee-profile")
    public ResponseEntity<MenteeProfileEntity> getMenteeProfile(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<MenteeProfileEntity> menteeProfileOptional =
                        menteeProfileRepository.findByUserUserId(userId);
                if (menteeProfileOptional.isPresent()) {
                    return new ResponseEntity<>(menteeProfileOptional.get(), HttpStatus.OK);
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
