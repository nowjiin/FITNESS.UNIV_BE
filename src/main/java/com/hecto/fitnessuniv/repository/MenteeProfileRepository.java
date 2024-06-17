package com.hecto.fitnessuniv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hecto.fitnessuniv.entity.MenteeProfileEntity;

public interface MenteeProfileRepository extends JpaRepository<MenteeProfileEntity, Long> {
    Optional<MenteeProfileEntity> findByUserUserId(String userid);
}
