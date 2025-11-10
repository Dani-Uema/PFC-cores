package com.example.paintlab.dto.user;

import java.util.UUID;


public record LoginResponseDTO(
        String token,
        UUID userId,
        String name,
        String email,
        String role
) {}