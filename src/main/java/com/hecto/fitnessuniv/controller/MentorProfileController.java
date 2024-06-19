package com.hecto.fitnessuniv.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

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

    @GetMapping("/mentor")
    public ResponseEntity<List<MentorProfileEntity>> getAllMentorsProfile() {
        List<MentorProfileEntity> mentors = service.getAllMentorsProfile();
        return new ResponseEntity<>(mentors, HttpStatus.OK);
    }

    @GetMapping("/mentor/{id}")
    public ResponseEntity<?> getMentorProfile(@PathVariable Long id) {
        Optional<MentorProfileEntity> mentorProfile = mentorProfileRepository.findById(id);
        if (mentorProfile.isPresent()) {
            return ResponseEntity.ok(mentorProfile.get());
        } else {
            return ResponseEntity.status(404).body("Mentor not found");
        }
    }

    @GetMapping("/mentor/{id}/user-id")
    public ResponseEntity<String> getMentorUserId(@PathVariable Long id) {
        Optional<MentorProfileEntity> mentorProfile = mentorProfileRepository.findById(id);
        if (mentorProfile.isPresent()) {
            UserEntity user = mentorProfile.get().getUser();
            return ResponseEntity.ok(user.getUserId());
        } else {
            return ResponseEntity.status(404).body("Mentor not found");
        }
    }

    @GetMapping("/mentor-profile")
    public ResponseEntity<MentorProfileEntity> getMentorProfile(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<MentorProfileEntity> mentorProfileOptional =
                        mentorProfileRepository.findByUserUserId(userId);
                if (mentorProfileOptional.isPresent()) {
                    return ResponseEntity.ok(mentorProfileOptional.get());
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("/mentor-profile")
    public ResponseEntity<?> updateMentorProfile(
            HttpServletRequest request, @RequestBody MentorProfileEntity updatedProfile) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<MentorProfileEntity> mentorProfileOptional =
                        mentorProfileRepository.findByUserUserId(userId);
                if (mentorProfileOptional.isPresent()) {
                    MentorProfileEntity mentorProfile = mentorProfileOptional.get();
                    // Update the mentor profile with the new details
                    mentorProfile.setRate(updatedProfile.getRate());
                    mentorProfile.setDetails(updatedProfile.getDetails());
                    // Save the updated profile
                    MentorProfileEntity savedProfile = mentorProfileRepository.save(mentorProfile);
                    return ResponseEntity.ok(savedProfile);
                } else {
                    return ResponseEntity.status(404).body("Mentor profile not found");
                }
            } else {
                return ResponseEntity.status(401).body("Invalid token");
            }
        } else {
            return ResponseEntity.status(400).body("Authorization header missing or invalid");
        }
    }
}
