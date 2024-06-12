package com.hecto.fitnessuniv.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hecto.fitnessuniv.entity.MentorProfileEntity;
import com.hecto.fitnessuniv.repository.MentorProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MentorProfileService {

    private final MentorProfileRepository mentorProfileRepository;

    public MentorProfileEntity saveMentorProfile(MentorProfileEntity mentorProfileEntity) {
        return mentorProfileRepository.save(mentorProfileEntity);
    }

    public List<MentorProfileEntity> getAllMentorsProfile() {
        List<MentorProfileEntity> mentors = mentorProfileRepository.findAll();
        mentors.forEach(
                mentor -> {
                    if (mentor.getUser() != null) {
                        mentor.setUserName(mentor.getUser().getUserName());
                    }
                });
        return mentors;
    }
}
