package com.hecto.fitnessuniv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.MentorProfile;
import com.hecto.fitnessuniv.service.MentorProfileService;

@RestController
@RequestMapping("/api/mentor")
public class MentorProfileController {

    @Autowired private MentorProfileService service;

    @PostMapping
    public ResponseEntity<MentorProfile> createMentorProfile(
            @RequestBody MentorProfile mentorProfile) {
        MentorProfile savedMentorProfile = service.saveMentorProfile(mentorProfile);
        return new ResponseEntity<>(savedMentorProfile, HttpStatus.CREATED);
    }
}
