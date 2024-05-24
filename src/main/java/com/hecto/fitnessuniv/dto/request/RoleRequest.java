package com.hecto.fitnessuniv.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleRequest {
    private String role;

    public RoleRequest() {}

    public RoleRequest(String role) {
        this.role = role;
    }
}
