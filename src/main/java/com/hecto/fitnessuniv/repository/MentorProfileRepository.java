package com.hecto.fitnessuniv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hecto.fitnessuniv.entity.MentorProfileEntity;
import com.hecto.fitnessuniv.entity.UserEntity;

public interface MentorProfileRepository extends JpaRepository<MentorProfileEntity, Long> {
    Optional<MentorProfileEntity> findByUser(UserEntity user);
}
