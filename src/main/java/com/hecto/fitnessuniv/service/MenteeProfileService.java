package com.hecto.fitnessuniv.service;

import org.springframework.stereotype.Service;

import com.hecto.fitnessuniv.entity.MenteeProfileEntity;
import com.hecto.fitnessuniv.repository.MenteeProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenteeProfileService {
    private final MenteeProfileRepository menteeProfileRepository;

    public MenteeProfileEntity saveMenteeProfile(MenteeProfileEntity menteeProfile) {
        return menteeProfileRepository.save(menteeProfile);
    }
}
