package com.hecto.fitnessuniv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.MentorProfile;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;
import com.hecto.fitnessuniv.service.MentorProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MentorProfileController {
    private final MentorProfileRepository mentorProfileRepository;
    private final JwtProvider jwtProvider;

    private final MentorProfileService service;

    @PostMapping("/mentor")
    public ResponseEntity<MentorProfile> createMentorProfile(
            @RequestBody MentorProfile mentorProfile) {
        MentorProfile savedMentorProfile = service.saveMentorProfile(mentorProfile);
        return new ResponseEntity<>(savedMentorProfile, HttpStatus.CREATED);
    }
}
