package com.hecto.fitnessuniv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hecto.fitnessuniv.entity.MentorProfile;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;

@Service
public class MentorProfileService {

    @Autowired private MentorProfileRepository repository;

    public MentorProfile saveMentorProfile(MentorProfile mentorProfile) {
        return repository.save(mentorProfile);
    }
}
