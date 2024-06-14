package com.hecto.fitnessuniv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hecto.fitnessuniv.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {}
