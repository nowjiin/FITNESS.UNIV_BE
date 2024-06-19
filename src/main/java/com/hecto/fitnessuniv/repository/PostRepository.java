package com.hecto.fitnessuniv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hecto.fitnessuniv.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {}
