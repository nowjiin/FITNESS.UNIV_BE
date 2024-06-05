package com.hecto.fitnessuniv.service;

import org.springframework.stereotype.Service;

import com.hecto.fitnessuniv.entity.MentorProfileEntity;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MentorProfileService {

    private final MentorProfileRepository repository;

    public MentorProfileEntity saveMentorProfile(MentorProfileEntity mentorProfileEntity) {
        return repository.save(mentorProfileEntity);
    }
}
