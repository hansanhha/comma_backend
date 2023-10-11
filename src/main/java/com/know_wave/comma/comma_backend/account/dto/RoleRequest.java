package com.know_wave.comma.comma_backend.account.dto;

import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import jakarta.validation.constraints.NotEmpty;

public class RoleRequest {

    @NotEmpty(message = "{Required}")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Role toRole() {
        switch (role) {
            case "admin" -> {
                return Role.ADMIN;
            }
            case "manager" -> {
                return Role.MANAGER;
            }
            default -> {
                return Role.MEMBER;
            }
        }
    }
}
