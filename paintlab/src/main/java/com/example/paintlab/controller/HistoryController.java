package com.example.paintlab.controller;

import com.example.paintlab.domain.ai.AIAnalysis;
import com.example.paintlab.domain.history.History;
import com.example.paintlab.dto.color.ColorSearchDTO;
import com.example.paintlab.dto.ai.AIHistoryDTO;
import com.example.paintlab.dto.HistoryDTO;
import com.example.paintlab.repositories.AIAnalysisRepository;
import com.example.paintlab.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HistoryController {

    private final HistoryService historyService;
    private final AIAnalysisRepository aiAnalysisRepository;

    @PostMapping("/color-search")
    public ResponseEntity<?> saveColorSearch(@RequestBody ColorSearchDTO dto) {
        try {
            historyService.saveColorSearch(dto);
            return ResponseEntity.ok("Consulta salva no histórico!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/ai-analysis")
    public ResponseEntity<?> saveAIAnalysis(@RequestBody AIHistoryDTO dto) {
        try {
            historyService.saveAIAnalysis(dto);
            return ResponseEntity.ok("Análise IA salva no histórico!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> saveHistory(@RequestBody HistoryDTO dto) {
        try {
            if (dto.hexCode() != null) {
                AIHistoryDTO aiDto = new AIHistoryDTO();
                aiDto.setUserId(dto.userId());
                aiDto.setHexCode(dto.hexCode());
                aiDto.setPigments(dto.pigments());
                historyService.saveAIAnalysis(aiDto);
                return ResponseEntity.ok("Análise IA salva no histórico!");
            } else if (dto.colorId() != null) {
                ColorSearchDTO colorDto = new ColorSearchDTO(dto.userId(), dto.colorId());
                historyService.saveColorSearch(colorDto);
                return ResponseEntity.ok("Consulta salva no histórico!");
            } else {
                return ResponseEntity.badRequest().body("Dados insuficientes");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getHistory(@PathVariable String userId) {
        try {
            UUID userUUID = UUID.fromString(userId.trim());

            List<History> searchHistory = historyService.getHistoryByUser(userUUID);

            List<AIAnalysis> aiHistory = aiAnalysisRepository.findByUserId(userUUID);

            Map<String, Object> result = new HashMap<>();
            result.put("searchHistory", searchHistory);
            result.put("aiHistory", aiHistory);
            result.put("totalSearch", searchHistory.size());
            result.put("totalAI", aiHistory.size());
            result.put("total", searchHistory.size() + aiHistory.size());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/item/{historyId}")
    public ResponseEntity<?> deleteHistoryItem(@PathVariable UUID historyId) {
        try {
            historyService.deleteHistoryItem(historyId);
            return ResponseEntity.ok("Item removido.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> clearHistory(@PathVariable UUID userId) {
        try {
            historyService.clearHistory(userId);
            return ResponseEntity.ok("Histórico apagado.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}