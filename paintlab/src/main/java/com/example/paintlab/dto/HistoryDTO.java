package com.example.paintlab.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record HistoryDTO (
        UUID userId,
        UUID colorId,
        String hexCode,
        List<Map<String, Object>> pigments
){ }