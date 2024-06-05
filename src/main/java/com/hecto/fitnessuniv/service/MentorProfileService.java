package com.hecto.fitnessuniv.service;

import org.springframework.stereotype.Service;

import com.hecto.fitnessuniv.entity.MentorProfile;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MentorProfileService {

    private final MentorProfileRepository repository;

    public MentorProfile saveMentorProfile(MentorProfile mentorProfile) {
        return repository.save(mentorProfile);
    }
}
