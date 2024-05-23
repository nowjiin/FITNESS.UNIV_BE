package com.hecto.fitnessuniv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hecto.fitnessuniv.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUserId(String userId);

    // user에 이메일 반환해주는 jwt filter에서 요청시
    UserEntity findByUserEmail(String email);
}
