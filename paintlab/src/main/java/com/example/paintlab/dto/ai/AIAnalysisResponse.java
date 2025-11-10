package com.example.paintlab.dto.ai;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AIAnalysisResponse {
    private String analyzedColor;
    private List<Map<String, Object>> pigments;
    private String source;
    private String timestamp;
    private boolean success;
    private String message;
}