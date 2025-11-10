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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ColorRepository colorRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AIAnalysisRepository aiAnalysisRepository;

    @InjectMocks
    private HistoryService historyService;

    private UUID userId;
    private UUID colorId;
    private UUID historyId;
    private User user;
    private Color color;
    private History history;
    private AIAnalysis aiAnalysis;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        colorId = UUID.randomUUID();
        historyId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        color = new Color();
        color.setId(colorId);
        color.setName("Azul Ultramar");
        color.setHexCode("#1F2A44");

        history = new History();
        history.setId(historyId);
        history.setUser(user);
        history.setColor(color);
        history.setConsultationDate(LocalDateTime.now());

        aiAnalysis = new AIAnalysis();
        aiAnalysis.setId(UUID.randomUUID());
        aiAnalysis.setUser(user);
        aiAnalysis.setHexCode("#FF5733");
        aiAnalysis.setPigmentsData("[{\"name\":\"Red\",\"proportion\":50.0}]");
        aiAnalysis.setAnalysisDate(LocalDateTime.now());
    }

    @Test
    void saveColorSearch_ShouldSaveHistory_WhenUserAndColorExist() {
        // Arrange
        ColorSearchDTO dto = new ColorSearchDTO(userId, colorId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));
        when(historyRepository.save(any(History.class))).thenReturn(history);

        // Act
        historyService.saveColorSearch(dto);

        // Assert
        verify(userRepository).findById(userId);
        verify(colorRepository).findById(colorId);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void saveColorSearch_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        ColorSearchDTO dto = new ColorSearchDTO(userId, colorId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historyService.saveColorSearch(dto);
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository).findById(userId);
        verify(colorRepository, never()).findById(any());
        verify(historyRepository, never()).save(any());
    }

    @Test
    void saveColorSearch_ShouldThrowException_WhenColorNotFound() {
        // Arrange
        ColorSearchDTO dto = new ColorSearchDTO(userId, colorId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(colorRepository.findById(colorId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historyService.saveColorSearch(dto);
        });

        assertTrue(exception.getMessage().contains("Cor não encontrada"));
        verify(userRepository).findById(userId);
        verify(colorRepository).findById(colorId);
        verify(historyRepository, never()).save(any());
    }

    @Test
    void saveAIAnalysis_ShouldSaveAnalysis_WhenValidData() throws Exception {
        // Arrange
        AIHistoryDTO dto = new AIHistoryDTO();
        dto.setUserId(userId);
        dto.setHexCode("#FF5733");
        dto.setPigments(List.of(Map.of("name", "Red", "proportion", 50.0)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.writeValueAsString(dto.getPigments())).thenReturn("[{\"name\":\"Red\",\"proportion\":50.0}]");
        when(aiAnalysisRepository.save(any(AIAnalysis.class))).thenReturn(aiAnalysis);

        // Act
        historyService.saveAIAnalysis(dto);

        // Assert
        verify(userRepository).findById(userId);
        verify(objectMapper).writeValueAsString(dto.getPigments());
        verify(aiAnalysisRepository).save(any(AIAnalysis.class));
    }

    @Test
    void saveAIAnalysis_ShouldSaveWithEmptyPigments_WhenPigmentsIsNull() {
        // Arrange
        AIHistoryDTO dto = new AIHistoryDTO();
        dto.setUserId(userId);
        dto.setHexCode("#FF5733");
        dto.setPigments(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(aiAnalysisRepository.save(any(AIAnalysis.class))).thenReturn(aiAnalysis);

        // Act
        historyService.saveAIAnalysis(dto);

        // Assert
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository).save(argThat(analysis ->
                "[]".equals(analysis.getPigmentsData())
        ));
    }

    @Test
    void saveAIAnalysis_ShouldSaveWithEmptyPigments_WhenPigmentsIsEmpty() {
        // Arrange
        AIHistoryDTO dto = new AIHistoryDTO();
        dto.setUserId(userId);
        dto.setHexCode("#FF5733");
        dto.setPigments(List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(aiAnalysisRepository.save(any(AIAnalysis.class))).thenReturn(aiAnalysis);

        // Act
        historyService.saveAIAnalysis(dto);

        // Assert
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository).save(argThat(analysis ->
                "[]".equals(analysis.getPigmentsData())
        ));
    }

    @Test
    void saveAIAnalysis_ShouldHandleJsonException() throws Exception {
        // Arrange
        AIHistoryDTO dto = new AIHistoryDTO();
        dto.setUserId(userId);
        dto.setHexCode("#FF5733");
        dto.setPigments(List.of(Map.of("name", "Red", "proportion", 50.0)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.writeValueAsString(dto.getPigments())).thenThrow(new RuntimeException("JSON error"));
        when(aiAnalysisRepository.save(any(AIAnalysis.class))).thenReturn(aiAnalysis);

        // Act
        historyService.saveAIAnalysis(dto);

        // Assert
        verify(userRepository).findById(userId);
        verify(objectMapper).writeValueAsString(dto.getPigments());
        verify(aiAnalysisRepository).save(argThat(analysis ->
                "[]".equals(analysis.getPigmentsData())
        ));
    }

    @Test
    void saveAIAnalysis_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        AIHistoryDTO dto = new AIHistoryDTO();
        dto.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historyService.saveAIAnalysis(dto);
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository, never()).save(any());
    }

    @Test
    void saveHistory_ShouldSaveAIAnalysis_WhenHexCodeProvided() {
        // Arrange
        HistoryDTO dto = new HistoryDTO(userId, null, "#FF5733", List.of(Map.of("name", "Red")));
        AIHistoryDTO aiDto = new AIHistoryDTO();
        aiDto.setUserId(userId);
        aiDto.setHexCode("#FF5733");
        aiDto.setPigments(List.of(Map.of("name", "Red")));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(aiAnalysisRepository.save(any(AIAnalysis.class))).thenReturn(aiAnalysis);

        // Act
        historyService.saveHistory(dto);

        // Assert
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository).save(any(AIAnalysis.class));
    }

    @Test
    void saveHistory_ShouldSaveColorSearch_WhenColorIdProvided() {
        // Arrange
        HistoryDTO dto = new HistoryDTO(userId, colorId, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));
        when(historyRepository.save(any(History.class))).thenReturn(history);

        // Act
        historyService.saveHistory(dto);

        // Assert
        verify(userRepository).findById(userId);
        verify(colorRepository).findById(colorId);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void saveHistory_ShouldThrowException_WhenInsufficientData() {
        // Arrange
        HistoryDTO dto = new HistoryDTO(userId, null, null, null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historyService.saveHistory(dto);
        });

        assertTrue(exception.getMessage().contains("Dados insuficientes"));
        verify(userRepository, never()).findById(any());
        verify(colorRepository, never()).findById(any());
        verify(aiAnalysisRepository, never()).save(any());
    }

    @Test
    void getHistoryByUser_ShouldReturnHistoryList() {
        // Arrange
        when(historyRepository.findByUserId(userId)).thenReturn(List.of(history));

        // Act
        List<History> result = historyService.getHistoryByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(historyId, result.get(0).getId());
        verify(historyRepository).findByUserId(userId);
    }

    @Test
    void getHistoryByUser_ShouldReturnEmptyList_WhenNoHistory() {
        // Arrange
        when(historyRepository.findByUserId(userId)).thenReturn(List.of());

        // Act
        List<History> result = historyService.getHistoryByUser(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(historyRepository).findByUserId(userId);
    }

    @Test
    void getAIHistoryByUser_ShouldReturnAIAnalysisList() {
        // Arrange
        List<AIAnalysis> allAnalyses = List.of(aiAnalysis);
        when(aiAnalysisRepository.findAll()).thenReturn(allAnalyses);

        // Act
        List<AIAnalysis> result = historyService.getAIHistoryByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(aiAnalysisRepository).findAll();
    }

    @Test
    void getAIHistoryByUser_ShouldReturnEmptyList_WhenNoAnalysisForUser() {
        // Arrange
        UUID otherUserId = UUID.randomUUID();
        when(aiAnalysisRepository.findAll()).thenReturn(List.of(aiAnalysis));

        // Act
        List<AIAnalysis> result = historyService.getAIHistoryByUser(otherUserId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(aiAnalysisRepository).findAll();
    }

    @Test
    void deleteHistoryItem_ShouldDeleteHistory() {
        // Arrange
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
        doNothing().when(historyRepository).delete(history);

        // Act
        historyService.deleteHistoryItem(historyId);

        // Assert
        verify(historyRepository).findById(historyId);
        verify(historyRepository).delete(history);
    }

    @Test
    void deleteHistoryItem_ShouldThrowException_WhenHistoryNotFound() {
        // Arrange
        when(historyRepository.findById(historyId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            historyService.deleteHistoryItem(historyId);
        });

        assertTrue(exception.getMessage().contains("Histórico não encontrado"));
        verify(historyRepository).findById(historyId);
        verify(historyRepository, never()).delete(any());
    }

    @Test
    void clearHistory_ShouldDeleteAllUserHistory() {
        // Arrange
        when(historyRepository.findByUserId(userId)).thenReturn(List.of(history));
        doNothing().when(historyRepository).deleteAll(List.of(history));

        // Act
        historyService.clearHistory(userId);

        // Assert
        verify(historyRepository).findByUserId(userId);
        verify(historyRepository).deleteAll(List.of(history));
    }

    @Test
    void clearHistory_ShouldHandleEmptyHistory() {
        // Arrange
        when(historyRepository.findByUserId(userId)).thenReturn(List.of());
        doNothing().when(historyRepository).deleteAll(List.of());

        // Act
        historyService.clearHistory(userId);

        // Assert
        verify(historyRepository).findByUserId(userId);
        verify(historyRepository).deleteAll(List.of());
    }
}
