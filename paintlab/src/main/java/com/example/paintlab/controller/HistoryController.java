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
        log.info("💾 [HISTORY] Salvando CONSULTA - User: {}, Color: {}", dto.userId(), dto.colorId());

        try {
            System.out.println("🔍 DTO recebido - UserId: " + dto.userId());
            System.out.println("🔍 DTO recebido - ColorId: " + dto.colorId());

            historyService.saveColorSearch(dto);

            System.out.println("✅ CONSULTA salva com sucesso no backend!");
            return ResponseEntity.ok("Consulta salva no histórico!");

        } catch (Exception e) {
            System.out.println("❌ ERRO no backend: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/ai-analysis")
    public ResponseEntity<?> saveAIAnalysis(@RequestBody AIHistoryDTO dto) {
        log.info("🤖 [HISTORY] Salvando ANÁLISE IA - User: {}, Hex: {}", dto.getUserId(), dto.getHexCode());

        try {
            historyService.saveAIAnalysis(dto);
            return ResponseEntity.ok("Análise IA salva no histórico!");
        } catch (Exception e) {
            log.error("❌ ERRO ao salvar análise IA: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> saveHistory(@RequestBody HistoryDTO dto) {
        log.info("🎯 [HISTORY] Salvando histórico genérico");

        try {
            if (dto.hexCode() != null) {
                // Converter para AIHistoryDTO
                AIHistoryDTO aiDto = new AIHistoryDTO();
                aiDto.setUserId(dto.userId());
                aiDto.setHexCode(dto.hexCode());
                aiDto.setPigments(dto.pigments());
                historyService.saveAIAnalysis(aiDto);
                return ResponseEntity.ok("Análise IA salva no histórico!");
            } else if (dto.colorId() != null) {
                // Converter para ColorSearchDTO
                ColorSearchDTO colorDto = new ColorSearchDTO(dto.userId(), dto.colorId());
                historyService.saveColorSearch(colorDto);
                return ResponseEntity.ok("Consulta salva no histórico!");
            } else {
                return ResponseEntity.badRequest().body("Dados insuficientes");
            }
        } catch (Exception e) {
            log.error("❌ ERRO no saveHistory: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getHistory(@PathVariable String userId) {
        log.info("🎯 CONTROLLER - Buscando histórico COMPLETO para user: {}", userId);

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

            log.info("✅ HISTÓRICO COMBINADO - Consultas: {}, IA: {}",
                    searchHistory.size(), aiHistory.size());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("❌ ERRO ao buscar histórico: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/item/{historyId}")
    public ResponseEntity<?> deleteHistoryItem(@PathVariable UUID historyId) {
        log.info("🗑️ Deletando item: {}", historyId);
        try {
            historyService.deleteHistoryItem(historyId);
            return ResponseEntity.ok("Item removido.");
        } catch (Exception e) {
            log.error("❌ ERRO ao deletar: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> clearHistory(@PathVariable UUID userId) {
        log.info("🗑️ Limpando histórico: {}", userId);
        try {
            historyService.clearHistory(userId);
            return ResponseEntity.ok("Histórico apagado.");
        } catch (Exception e) {
            log.error("❌ ERRO ao limpar: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}