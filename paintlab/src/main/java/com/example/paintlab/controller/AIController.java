package com.example.paintlab.controller;

import com.example.paintlab.domain.ai.AIAnalysis;
import com.example.paintlab.domain.user.User;
import com.example.paintlab.dto.ai.AIAnalysisRequest;
import com.example.paintlab.dto.ai.AIAnalysisResponse;
import com.example.paintlab.dto.ai.AIHistoryDTO;
import com.example.paintlab.repositories.AIAnalysisRepository;
import com.example.paintlab.repositories.UserRepository;
import com.example.paintlab.service.AIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AIController {

    private final AIService aiService;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/analyzed-cor")
    public ResponseEntity<AIAnalysisResponse> analyzeColor(@RequestBody AIAnalysisRequest request) {
        try {
            if (request.getHexCode() == null || !request.getHexCode().matches("^#[0-9A-Fa-f]{6}$")) {
                throw new IllegalArgumentException("Código HEX inválido");
            }

            var resultado = aiService.analyzedColorWithAI(request.getHexCode());

            AIAnalysisResponse response = new AIAnalysisResponse();
            response.setAnalyzedColor(getStringSafe(resultado, "cor_analisada"));
            response.setPigments(getListSafe(resultado, "pigmentos"));
            response.setSource(getStringSafe(resultado, "fonte"));
            response.setTimestamp(getObjectSafe(resultado, "timestamp").toString());
            response.setSuccess(true);
            response.setMessage("Análise concluída com sucesso");

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            AIAnalysisResponse errorResponse = new AIAnalysisResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Erro na análise: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/history")
    public ResponseEntity<?> saveAIHistory(@RequestBody AIHistoryDTO dto) {
        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> {
                        return new RuntimeException("User not found: " + dto.getUserId());
                    });
            AIAnalysis analysis = new AIAnalysis();
            analysis.setUser(user);
            analysis.setHexCode(dto.getHexCode());

            String pigmentsJson = objectMapper.writeValueAsString(dto.getPigments());

            analysis.setPigmentsData(pigmentsJson);
            analysis.setAnalysisDate(LocalDateTime.now());

            AIAnalysis saved = aiAnalysisRepository.save(analysis);

            return ResponseEntity.ok("AI analysis saved to history");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getAIHistory(@PathVariable UUID userId) {
        try {
            List<AIAnalysis> history = aiAnalysisRepository.findByUserId(userId);
            return ResponseEntity.ok(history);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private String getStringSafe(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getListSafe(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof List) {
            return (List<Map<String, Object>>) value;
        }
        throw new ClassCastException("Valor não é uma List: " + key);
    }

    private Object getObjectSafe(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value != null) {
            return value;
        }
        throw new IllegalArgumentException("Chave não encontrada: " + key);
    }
}