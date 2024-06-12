package com.hecto.fitnessuniv.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hecto.fitnessuniv.entity.MenteeProfileEntity;
import com.hecto.fitnessuniv.repository.MenteeProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenteeProfileService {
    private final MenteeProfileRepository repository;

    public MenteeProfileEntity saveMenteeProfile(MenteeProfileEntity menteeProfile) {
        return repository.save(menteeProfile);
    }

    public List<MenteeProfileEntity> getAllMenteesProfile() {
        return repository.findAll();
    }
}
