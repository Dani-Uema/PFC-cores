package com.example.paintlab.service;

import com.example.paintlab.domain.ai.AIAnalysis;
import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.history.History;
import com.example.paintlab.domain.user.User;
import com.example.paintlab.dto.color.ColorSearchDTO;
import com.example.paintlab.dto.ai.AIHistoryDTO;
import com.example.paintlab.dto.HistoryDTO;
import com.example.paintlab.repositories.AIAnalysisRepository;
import com.example.paintlab.repositories.ColorRepository;
import com.example.paintlab.repositories.HistoryRepository;
import com.example.paintlab.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ColorRepository colorRepository;
    private final ObjectMapper objectMapper;
    private final AIAnalysisRepository aiAnalysisRepository;

    public void saveColorSearch(ColorSearchDTO dto) {
        System.out.println("💾 [HISTORY SERVICE] Salvando CONSULTA...");

        try {
            System.out.println("1. 🔍 Buscando usuário: " + dto.userId());
            User user = userRepository.findById(dto.userId())
                    .orElseThrow(() -> {
                        System.out.println("❌ USUÁRIO NÃO ENCONTRADO!");
                        return new RuntimeException("Usuário não encontrado");
                    });
            System.out.println("✅ Usuário encontrado: " + user.getEmail());

            System.out.println("2. 🔍 Buscando cor: " + dto.colorId());
            Color color = colorRepository.findById(dto.colorId())
                    .orElseThrow(() -> {
                        System.out.println("❌ COR NÃO ENCONTRADA!");
                        return new RuntimeException("Cor não encontrada");
                    });
            System.out.println("✅ Cor encontrada: " + color.getName());

            System.out.println("3. 🏗️ Criando histórico...");
            History history = new History(user, color);

            System.out.println("4. 💾 Salvando no banco...");
            History savedHistory = historyRepository.save(history);

            System.out.println("✅ CONSULTA salva! ID: " + savedHistory.getId());

        } catch (Exception e) {
            System.out.println("❌ ERRO ao salvar consulta: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void saveAIAnalysis(AIHistoryDTO dto) {
        System.out.println("🤖 [HISTORY SERVICE] Salvando ANÁLISE IA...");

        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            AIAnalysis analysis = new AIAnalysis();
            analysis.setUser(user);
            analysis.setHexCode(dto.getHexCode());
            analysis.setAnalysisDate(LocalDateTime.now());

            if (dto.getPigments() != null && !dto.getPigments().isEmpty()) {
                try {
                    String pigmentsJson = objectMapper.writeValueAsString(dto.getPigments());
                    analysis.setPigmentsData(pigmentsJson);
                } catch (Exception e) {
                    analysis.setPigmentsData("[]");
                }
            } else {
                analysis.setPigmentsData("[]");
            }

            AIAnalysis savedAnalysis = aiAnalysisRepository.save(analysis);
            System.out.println("✅ ANÁLISE IA salva! ID: " + savedAnalysis.getId());

        } catch (Exception e) {
            System.out.println("❌ ERRO ao salvar análise IA: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void saveHistory(HistoryDTO dto) {
        System.out.println("🎯 [HISTORY] Salvando histórico genérico");

        if (dto.hexCode() != null) {
            AIHistoryDTO aiDto = new AIHistoryDTO();
            aiDto.setUserId(dto.userId());
            aiDto.setHexCode(dto.hexCode());
            aiDto.setPigments(dto.pigments());
            saveAIAnalysis(aiDto);
        } else if (dto.colorId() != null) {
            ColorSearchDTO colorDto = new ColorSearchDTO(dto.userId(), dto.colorId());
            saveColorSearch(colorDto);
        } else {
            throw new RuntimeException("Dados insuficientes");
        }
    }

    public List<History> getHistoryByUser(UUID userId) {
        System.out.println("🎯 SERVICE - Buscando histórico para user UUID: " + userId);

        try {
            List<History> history = historyRepository.findByUserId(userId);
            System.out.println("✅ Histórico encontrado: " + history.size() + " registros");

            for (History h : history) {
                System.out.println("   📝 Item: " + h.getId() +
                        " - Cor: " + (h.getColor() != null ? h.getColor().getName() : "NULL") +
                        " - Data: " + h.getConsultationDate());
            }

            return history;
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar histórico: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AIAnalysis> getAIHistoryByUser(UUID userId) {
        System.out.println("🎯 SERVICE - Buscando histórico IA para user: " + userId);

        try {
            List<AIAnalysis> aiHistory = aiAnalysisRepository.findAll().stream()
                    .filter(analysis -> analysis.getUser().getId().equals(userId))
                    .sorted((a, b) -> b.getAnalysisDate().compareTo(a.getAnalysisDate()))
                    .collect(Collectors.toList());

            System.out.println("✅ Histórico IA encontrado: " + aiHistory.size() + " itens");
            return aiHistory;

        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar histórico IA: " + e.getMessage());
            throw e;
        }
    }

    public void deleteHistoryItem(UUID historyId) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado"));
        historyRepository.delete(history);
    }

    public void clearHistory(UUID userId) {
        System.out.println("🗑️ Limpando histórico para user: " + userId);
        List<History> historyList = historyRepository.findByUserId(userId);
        historyRepository.deleteAll(historyList);
        System.out.println("✅ Histórico limpo!");
    }
}