package com.hecto.fitnessuniv.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/check-user-role")
    public ResponseEntity<String> checkUserExists(@RequestParam String userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            return ResponseEntity.ok(userEntity.getUserRole());
        } else {
            return ResponseEntity.ok("ROLE_NEW");
        }
    }
}
