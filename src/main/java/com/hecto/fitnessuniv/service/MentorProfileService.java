package com.hecto.fitnessuniv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hecto.fitnessuniv.entity.MentorProfile;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;

@Service
@RequiredArgsConstructor
public class MentorProfileService {

    private final MentorProfileRepository repository;

    public MentorProfile saveMentorProfile(MentorProfile mentorProfile) {
        return repository.save(mentorProfile);
    }
}
