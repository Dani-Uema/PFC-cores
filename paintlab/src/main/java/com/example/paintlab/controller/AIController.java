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

    @PostMapping("/analisar-cor")
    public ResponseEntity<AIAnalysisResponse> analisarCor(@RequestBody AIAnalysisRequest request) {
        log.info("🎯 Recebida requisição de análise IA - Hex: {}", request.getHexCode());

        try {
            if (request.getHexCode() == null || !request.getHexCode().matches("^#[0-9A-Fa-f]{6}$")) {
                throw new IllegalArgumentException("Código HEX inválido");
            }

            var resultado = aiService.analisarCorComIA(request.getHexCode());

            AIAnalysisResponse response = new AIAnalysisResponse();
            response.setAnalyzedColor(getStringSafe(resultado, "cor_analisada"));
            response.setPigments(getListSafe(resultado, "pigmentos"));
            response.setSource(getStringSafe(resultado, "fonte"));
            response.setTimestamp(getObjectSafe(resultado, "timestamp").toString());
            response.setSuccess(true);
            response.setMessage("Análise concluída com sucesso");

            log.info("✅ Resposta IA enviada: {}", response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Erro no controller IA: {}", e.getMessage());

            AIAnalysisResponse errorResponse = new AIAnalysisResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Erro na análise: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/history")
    public ResponseEntity<?> saveAIHistory(@RequestBody AIHistoryDTO dto) {
        log.info("🎯 Salvando histórico AI - User: {}, Cor: {}", dto.getUserId(), dto.getHexCode());

        try {
            // ✅ DEBUG DETALHADO
            System.out.println("🔍 DTO recebido no AIController:");
            System.out.println("   - UserId: " + dto.getUserId());
            System.out.println("   - HexCode: " + dto.getHexCode());
            System.out.println("   - Pigments: " + dto.getPigments());
            System.out.println("   - Source: " + dto.getSource());

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> {
                        System.out.println("❌ USUÁRIO NÃO ENCONTRADO: " + dto.getUserId());
                        return new RuntimeException("User not found: " + dto.getUserId());
                    });
            System.out.println("✅ Usuário encontrado: " + user.getEmail());

            AIAnalysis analysis = new AIAnalysis();
            analysis.setUser(user);
            analysis.setHexCode(dto.getHexCode());

            // ✅ DEBUG do pigmentsData
            System.out.println("🔍 Convertendo pigments para JSON...");
            String pigmentsJson = objectMapper.writeValueAsString(dto.getPigments());
            System.out.println("✅ Pigments JSON: " + pigmentsJson);

            analysis.setPigmentsData(pigmentsJson);
            analysis.setAnalysisDate(LocalDateTime.now());

            System.out.println("💾 Salvando AIAnalysis...");
            AIAnalysis saved = aiAnalysisRepository.save(analysis);

            System.out.println("✅ Histórico AI salvo com sucesso! ID: " + saved.getId());
            return ResponseEntity.ok("AI analysis saved to history");

        } catch (Exception e) {
            System.out.println("❌ ERRO CRÍTICO ao salvar histórico AI:");
            System.out.println("❌ Mensagem: " + e.getMessage());
            System.out.println("❌ Classe: " + e.getClass().getName());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getAIHistory(@PathVariable UUID userId) {
        log.info("🎯 Buscando histórico AI para user: {}", userId);

        try {
            List<AIAnalysis> history = aiAnalysisRepository.findByUserId(userId);
            log.info("✅ Histórico AI encontrado: {} análises", history.size());
            return ResponseEntity.ok(history);

        } catch (Exception e) {
            log.error("❌ Erro ao buscar histórico AI: {}", e.getMessage());
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