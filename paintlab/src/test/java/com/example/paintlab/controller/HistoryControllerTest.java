package com.example.paintlab.controller;

import com.example.paintlab.domain.ai.AIAnalysis;
import com.example.paintlab.domain.history.History;
import com.example.paintlab.dto.color.ColorSearchDTO;
import com.example.paintlab.dto.ai.AIHistoryDTO;
import com.example.paintlab.dto.HistoryDTO;
import com.example.paintlab.repositories.AIAnalysisRepository;
import com.example.paintlab.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    @Mock
    private HistoryService historyService;

    @Mock
    private AIAnalysisRepository aiAnalysisRepository;

    @InjectMocks
    private HistoryController historyController;

    private UUID userId;
    private UUID colorId;
    private UUID historyId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        colorId = UUID.randomUUID();
        historyId = UUID.randomUUID();
    }

    @Test
    void saveColorSearch_ShouldReturnOk_WhenSuccessful() {
        // Arrange
        ColorSearchDTO dto = new ColorSearchDTO(userId, colorId);
        doNothing().when(historyService).saveColorSearch(dto);

        // Act
        ResponseEntity<?> response = historyController.saveColorSearch(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Consulta salva no histórico!", response.getBody());
        verify(historyService).saveColorSearch(dto);
    }

    @Test
    void saveColorSearch_ShouldReturnBadRequest_WhenExceptionOccurs() {
        // Arrange
        ColorSearchDTO dto = new ColorSearchDTO(userId, colorId);
        doThrow(new RuntimeException("Erro de banco")).when(historyService).saveColorSearch(dto);

        // Act
        ResponseEntity<?> response = historyController.saveColorSearch(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro"));
        verify(historyService).saveColorSearch(dto);
    }

    @Test
    void saveAIAnalysis_ShouldReturnOk_WhenSuccessful() {
        // Arrange
        AIHistoryDTO dto = new AIHistoryDTO();
        dto.setUserId(userId);
        dto.setHexCode("#FF5733");
        doNothing().when(historyService).saveAIAnalysis(dto);

        // Act
        ResponseEntity<?> response = historyController.saveAIAnalysis(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Análise IA salva no histórico!", response.getBody());
        verify(historyService).saveAIAnalysis(dto);
    }

    @Test
    void saveAIAnalysis_ShouldReturnBadRequest_WhenExceptionOccurs() {
        // Arrange
        AIHistoryDTO dto = new AIHistoryDTO();
        dto.setUserId(userId);
        dto.setHexCode("#FF5733");
        doThrow(new RuntimeException("Erro de IA")).when(historyService).saveAIAnalysis(dto);

        // Act
        ResponseEntity<?> response = historyController.saveAIAnalysis(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro"));
        verify(historyService).saveAIAnalysis(dto);
    }

    @Test
    void saveHistory_ShouldSaveAIAnalysis_WhenHexCodeProvided() {
        // Arrange
        HistoryDTO dto = new HistoryDTO(userId, null, "#FF5733", null);
        AIHistoryDTO aiDto = new AIHistoryDTO();
        aiDto.setUserId(userId);
        aiDto.setHexCode("#FF5733");

        doNothing().when(historyService).saveAIAnalysis(any(AIHistoryDTO.class));

        // Act
        ResponseEntity<?> response = historyController.saveHistory(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Análise IA salva no histórico!", response.getBody());
        verify(historyService).saveAIAnalysis(any(AIHistoryDTO.class));
    }

    @Test
    void saveHistory_ShouldSaveColorSearch_WhenColorIdProvided() {
        // Arrange
        HistoryDTO dto = new HistoryDTO(userId, colorId, null, null);
        ColorSearchDTO colorDto = new ColorSearchDTO(userId, colorId);

        doNothing().when(historyService).saveColorSearch(any(ColorSearchDTO.class));

        // Act
        ResponseEntity<?> response = historyController.saveHistory(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Consulta salva no histórico!", response.getBody());
        verify(historyService).saveColorSearch(any(ColorSearchDTO.class));
    }

    @Test
    void saveHistory_ShouldReturnBadRequest_WhenInsufficientData() {
        // Arrange
        HistoryDTO dto = new HistoryDTO(userId, null, null, null);

        // Act
        ResponseEntity<?> response = historyController.saveHistory(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados insuficientes", response.getBody());
        verify(historyService, never()).saveAIAnalysis(any());
        verify(historyService, never()).saveColorSearch(any());
    }

    @Test
    void getHistory_ShouldReturnCombinedHistory_WhenUserExists() {
        // Arrange
        History searchHistory = new History();
        AIAnalysis aiAnalysis = new AIAnalysis();

        List<History> searchHistoryList = List.of(searchHistory);
        List<AIAnalysis> aiHistoryList = List.of(aiAnalysis);

        when(historyService.getHistoryByUser(userId)).thenReturn(searchHistoryList);
        when(aiAnalysisRepository.findByUserId(userId)).thenReturn(aiHistoryList);

        // Act
        ResponseEntity<?> response = historyController.getHistory(userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) response.getBody();

        assertEquals(searchHistoryList, result.get("searchHistory"));
        assertEquals(aiHistoryList, result.get("aiHistory"));
        assertEquals(1, result.get("totalSearch"));
        assertEquals(1, result.get("totalAI"));
        assertEquals(2, result.get("total"));

        verify(historyService).getHistoryByUser(userId);
        verify(aiAnalysisRepository).findByUserId(userId);
    }

    @Test
    void getHistory_ShouldReturnBadRequest_WhenInvalidUserId() {
        // Arrange
        String invalidUserId = "invalid-uuid";

        // Act
        ResponseEntity<?> response = historyController.getHistory(invalidUserId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro"));
    }

    @Test
    void deleteHistoryItem_ShouldReturnOk_WhenSuccessful() {
        // Arrange
        doNothing().when(historyService).deleteHistoryItem(historyId);

        // Act
        ResponseEntity<?> response = historyController.deleteHistoryItem(historyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item removido.", response.getBody());
        verify(historyService).deleteHistoryItem(historyId);
    }

    @Test
    void deleteHistoryItem_ShouldReturnBadRequest_WhenExceptionOccurs() {
        // Arrange
        doThrow(new RuntimeException("Item não encontrado")).when(historyService).deleteHistoryItem(historyId);

        // Act
        ResponseEntity<?> response = historyController.deleteHistoryItem(historyId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro"));
        verify(historyService).deleteHistoryItem(historyId);
    }

    @Test
    void clearHistory_ShouldReturnOk_WhenSuccessful() {
        // Arrange
        doNothing().when(historyService).clearHistory(userId);

        // Act
        ResponseEntity<?> response = historyController.clearHistory(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Histórico apagado.", response.getBody());
        verify(historyService).clearHistory(userId);
    }

    @Test
    void clearHistory_ShouldReturnBadRequest_WhenExceptionOccurs() {
        // Arrange
        doThrow(new RuntimeException("Erro ao limpar")).when(historyService).clearHistory(userId);

        // Act
        ResponseEntity<?> response = historyController.clearHistory(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro"));
        verify(historyService).clearHistory(userId);
    }

    @Test
    void getHistory_ShouldReturnEmptyResults_WhenNoHistoryExists() {
        // Arrange
        when(historyService.getHistoryByUser(userId)).thenReturn(List.of());
        when(aiAnalysisRepository.findByUserId(userId)).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = historyController.getHistory(userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) response.getBody();

        assertEquals(0, result.get("totalSearch"));
        assertEquals(0, result.get("totalAI"));
        assertEquals(0, result.get("total"));
    }
}
