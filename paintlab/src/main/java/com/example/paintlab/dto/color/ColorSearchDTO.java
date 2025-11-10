package com.example.paintlab.dto.color;

import java.util.UUID;

public record ColorSearchDTO(
        UUID userId,
        UUID colorId
) {}