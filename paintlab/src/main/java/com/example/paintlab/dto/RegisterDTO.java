package com.example.paintlab.dto;

import com.example.paintlab.domain.user.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
