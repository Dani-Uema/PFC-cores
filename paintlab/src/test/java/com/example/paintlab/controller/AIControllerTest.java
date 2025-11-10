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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AIControllerTest {

    @Mock
    private AIService aiService;

    @Mock
    private AIAnalysisRepository aiAnalysisRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AIController aiController;

    private UUID userId;
    private User user;
    private AIAnalysisRequest analysisRequest;
    private AIHistoryDTO aiHistoryDTO;
    private Map<String, Object> aiResult;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@email.com");

        analysisRequest = new AIAnalysisRequest();
        analysisRequest.setHexCode("#FF5733");

        aiHistoryDTO = new AIHistoryDTO();
        aiHistoryDTO.setUserId(userId);
        aiHistoryDTO.setHexCode("#FF5733");
        aiHistoryDTO.setPigments(List.of(
                Map.of("name", "Cadmium Red", "proportion", 60.0),
                Map.of("name", "Quinacridone", "proportion", 40.0)
        ));
        aiHistoryDTO.setSource("AI Analysis");

        aiResult = new HashMap<>();
        aiResult.put("cor_analisada", "#FF5733");
        aiResult.put("pigmentos", List.of(
                Map.of("nome", "Cadmium Red", "proporcao", 60.0),
                Map.of("nome", "Quinacridone", "proporcao", 40.0)
        ));
        aiResult.put("fonte", "AI Model v1.0");
        aiResult.put("timestamp", LocalDateTime.now());
    }

    @Test
    void analisarCor_ShouldReturnSuccessResponse_WhenValidHexCode() {
        // Arrange
        when(aiService.analisarCorComIA("#FF5733")).thenReturn(aiResult);

        // Act
        ResponseEntity<AIAnalysisResponse> response = aiController.analisarCor(analysisRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("#FF5733", response.getBody().getAnalyzedColor());
        assertEquals("Análise concluída com sucesso", response.getBody().getMessage());
        verify(aiService).analisarCorComIA("#FF5733");
    }

    @Test
    void analisarCor_ShouldReturnErrorResponse_WhenInvalidHexCode() {
        // Arrange
        analysisRequest.setHexCode("invalid-hex");

        // Act
        ResponseEntity<AIAnalysisResponse> response = aiController.analisarCor(analysisRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Código HEX inválido"));
        verify(aiService, never()).analisarCorComIA(anyString());
    }

    @Test
    void analisarCor_ShouldReturnErrorResponse_WhenNullHexCode() {
        // Arrange
        analysisRequest.setHexCode(null);

        // Act
        ResponseEntity<AIAnalysisResponse> response = aiController.analisarCor(analysisRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Código HEX inválido"));
    }

    @Test
    void analisarCor_ShouldReturnErrorResponse_WhenAIServiceFails() {
        // Arrange
        when(aiService.analisarCorComIA("#FF5733"))
                .thenThrow(new RuntimeException("AI Service unavailable"));

        // Act
        ResponseEntity<AIAnalysisResponse> response = aiController.analisarCor(analysisRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("AI Service unavailable"));
        verify(aiService).analisarCorComIA("#FF5733");
    }

    @Test
    void analisarCor_ShouldHandlePartialAIResult() {
        // Arrange
        Map<String, Object> partialResult = new HashMap<>();
        partialResult.put("cor_analisada", "#FF5733");
        partialResult.put("pigmentos", List.of()); // Lista vazia em vez de null
        partialResult.put("fonte", "AI Model");
        partialResult.put("timestamp", LocalDateTime.now());

        when(aiService.analisarCorComIA("#FF5733")).thenReturn(partialResult);

        // Act
        ResponseEntity<AIAnalysisResponse> response = aiController.analisarCor(analysisRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("#FF5733", response.getBody().getAnalyzedColor());
        assertNotNull(response.getBody().getPigments());
        assertTrue(response.getBody().getPigments().isEmpty());
        assertEquals("AI Model", response.getBody().getSource());
    }

    @Test
    void analisarCor_ShouldHandleMinimalAIResult() {
        // Arrange
        Map<String, Object> minimalResult = new HashMap<>();
        minimalResult.put("cor_analisada", "#FF5733");
        minimalResult.put("pigmentos", List.of()); // Lista vazia em vez de null
        minimalResult.put("timestamp", LocalDateTime.now());

        when(aiService.analisarCorComIA("#FF5733")).thenReturn(minimalResult);

        // Act
        ResponseEntity<AIAnalysisResponse> response = aiController.analisarCor(analysisRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("#FF5733", response.getBody().getAnalyzedColor());
        assertNotNull(response.getBody().getPigments()); // Deve ser lista vazia
        assertTrue(response.getBody().getPigments().isEmpty());
    }
    @Test
    void saveAIHistory_ShouldReturnSuccess_WhenValidData() throws Exception {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.writeValueAsString(aiHistoryDTO.getPigments()))
                .thenReturn("[{\"name\":\"Cadmium Red\",\"proportion\":60.0}]");
        when(aiAnalysisRepository.save(any(AIAnalysis.class))).thenAnswer(invocation -> {
            AIAnalysis analysis = invocation.getArgument(0);
            analysis.setId(UUID.randomUUID());
            return analysis;
        });

        // Act
        ResponseEntity<?> response = aiController.saveAIHistory(aiHistoryDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AI analysis saved to history", response.getBody());
        verify(userRepository).findById(userId);
        verify(objectMapper).writeValueAsString(aiHistoryDTO.getPigments());
        verify(aiAnalysisRepository).save(any(AIAnalysis.class));
    }

    @Test
    void saveAIHistory_ShouldReturnError_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = aiController.saveAIHistory(aiHistoryDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository, never()).save(any(AIAnalysis.class));
    }

    @Test
    void saveAIHistory_ShouldReturnError_WhenJsonConversionFails() throws Exception {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.writeValueAsString(aiHistoryDTO.getPigments()))
                .thenThrow(new RuntimeException("JSON conversion error"));

        // Act
        ResponseEntity<?> response = aiController.saveAIHistory(aiHistoryDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("JSON conversion error"));
        verify(userRepository).findById(userId);
        verify(objectMapper).writeValueAsString(aiHistoryDTO.getPigments());
        verify(aiAnalysisRepository, never()).save(any(AIAnalysis.class));
    }

    @Test
    void saveAIHistory_ShouldHandleNullPigments() throws Exception {
        // Arrange
        aiHistoryDTO.setPigments(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.writeValueAsString(null))
                .thenReturn("null");
        when(aiAnalysisRepository.save(any(AIAnalysis.class))).thenReturn(new AIAnalysis());

        // Act
        ResponseEntity<?> response = aiController.saveAIHistory(aiHistoryDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(objectMapper).writeValueAsString(null);
        verify(aiAnalysisRepository).save(any(AIAnalysis.class));
    }

    @Test
    void getAIHistory_ShouldReturnHistoryList_WhenUserExists() {
        // Arrange
        AIAnalysis analysis = new AIAnalysis();
        analysis.setId(UUID.randomUUID());
        analysis.setUser(user);
        analysis.setHexCode("#FF5733");

        List<AIAnalysis> historyList = List.of(analysis);
        when(aiAnalysisRepository.findByUserId(userId)).thenReturn(historyList);

        // Act
        ResponseEntity<?> response = aiController.getAIHistory(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(historyList, response.getBody());
        verify(aiAnalysisRepository).findByUserId(userId);
    }

    @Test
    void getAIHistory_ShouldReturnEmptyList_WhenNoHistoryExists() {
        // Arrange
        when(aiAnalysisRepository.findByUserId(userId)).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = aiController.getAIHistory(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(aiAnalysisRepository).findByUserId(userId);
    }

    @Test
    void getAIHistory_ShouldReturnError_WhenRepositoryFails() {
        // Arrange
        when(aiAnalysisRepository.findByUserId(userId))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = aiController.getAIHistory(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Database error"));
        verify(aiAnalysisRepository).findByUserId(userId);
    }

    @Test
    void analisarCor_ShouldHandleVariousDataTypesInAIResult() {
        // Arrange
        Map<String, Object> complexResult = new HashMap<>();
        complexResult.put("cor_analisada", 12345); // Número - getStringSafe converte para "12345"
        complexResult.put("pigmentos", List.of(Map.of("nome", "Test"))); // Lista válida
        complexResult.put("fonte", new Object() { // Objeto com toString
            @Override
            public String toString() {
                return "Test Object";
            }
        });
        complexResult.put("timestamp", "2023-01-01"); // String

        when(aiService.analisarCorComIA("#FF5733")).thenReturn(complexResult);

        // Act
        ResponseEntity<AIAnalysisResponse> response = aiController.analisarCor(analysisRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("12345", response.getBody().getAnalyzedColor()); // Número convertido para string
        assertEquals("Test Object", response.getBody().getSource()); // Objeto convertido para string
    }
}