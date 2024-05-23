package com.hecto.fitnessuniv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hecto.fitnessuniv.dto.request.RoleRequest;

@RestController
@RequestMapping("/api")
public class RoleController {
    @PostMapping("/role")
    public ResponseEntity<String> receiveRole(@RequestBody RoleRequest roleRequest) {
        // 받은 역할을 처리하는 로직을 여기에 추가합니다.
        System.out.println("Received role: " + roleRequest.getRole());

        // 성공적으로 처리된 경우 응답을 반환합니다.
        return new ResponseEntity<>("Role received: " + roleRequest.getRole(), HttpStatus.OK);
    }
}
