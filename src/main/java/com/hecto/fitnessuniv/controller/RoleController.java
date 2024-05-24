package com.hecto.fitnessuniv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.dto.request.RoleRequest;
import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
public class RoleController {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @PostMapping("/role")
    public ResponseEntity<String> updateRole(
            @RequestBody RoleRequest roleRequest, @RequestHeader("Authorization") String token) {
        // Jwt 토큰에서 userId 추출
        String userId = jwtProvider.getUserIdFromToken(token.substring(7));
        // 가져온 아이디가 있나 조회
        UserEntity userEntity =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        // 역할 업데이트
        System.out.println("역할 업데이트됨 !");
        userEntity.setRole(roleRequest.getRole());
        userRepository.save(userEntity);

        return new ResponseEntity<>("Role updated", HttpStatus.OK);
    }
}
