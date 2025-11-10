package com.example.paintlab.dto.ai;


import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class AIHistoryDTO {
    private UUID userId;
    private String hexCode;
    private List<Map<String, Object>> pigments;
    private String source = "AI";
}
