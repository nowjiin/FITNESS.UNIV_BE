package com.hecto.fitnessuniv.dto;

import java.security.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String type;
    private String role;
    private Timestamp createdAt;
}
