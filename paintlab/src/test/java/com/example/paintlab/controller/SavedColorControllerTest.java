package com.example.paintlab.controller;

import com.example.paintlab.dto.SavedColorDTO;
import com.example.paintlab.service.SavedColorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavedColorControllerTest {

    @Mock
    private SavedColorService savedColorService;

    @InjectMocks
    private SavedColorController savedColorController;

    private UUID userId;
    private UUID colorId;
    private SavedColorDTO savedColorDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        colorId = UUID.randomUUID();

        savedColorDTO = new SavedColorDTO("UB05", "Azul Marinho", "sala", "Cor para parede da sala");
        savedColorDTO.setId(colorId);
        savedColorDTO.setHexCode("#1F2A44");
        savedColorDTO.setSavedDate(LocalDateTime.now());
    }

    @Test
    void saveColor_ShouldReturnSavedColor_WhenSuccessful() {
        // Arrange
        when(savedColorService.saveColor(any(SavedColorDTO.class), eq(userId))).thenReturn(savedColorDTO);

        // Act
        ResponseEntity<?> response = savedColorController.saveColor(savedColorDTO, userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedColorDTO, response.getBody());
        verify(savedColorService).saveColor(savedColorDTO, userId);
    }

    @Test
    void saveColor_ShouldReturnBadRequest_WhenInvalidUserId() {
        // Arrange
        String invalidUserId = "invalid-uuid";

        // Act
        ResponseEntity<?> response = savedColorController.saveColor(savedColorDTO, invalidUserId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao salvar cor"));
        verify(savedColorService, never()).saveColor(any(), any());
    }

    @Test
    void saveColor_ShouldReturnBadRequest_WhenServiceThrowsException() {
        // Arrange
        when(savedColorService.saveColor(any(SavedColorDTO.class), eq(userId)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = savedColorController.saveColor(savedColorDTO, userId.toString());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao salvar cor"));
        verify(savedColorService).saveColor(savedColorDTO, userId);
    }

    @Test
    void getSavedColors_ShouldReturnColorList_WhenSuccessful() {
        // Arrange
        List<SavedColorDTO> savedColors = List.of(savedColorDTO);
        when(savedColorService.findByUserId(userId)).thenReturn(savedColors);

        // Act
        ResponseEntity<?> response = savedColorController.getSavedColors(userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedColors, response.getBody());
        verify(savedColorService).findByUserId(userId);
    }

    @Test
    void getSavedColors_ShouldReturnEmptyList_WhenNoColorsSaved() {
        // Arrange
        when(savedColorService.findByUserId(userId)).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = savedColorController.getSavedColors(userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(savedColorService).findByUserId(userId);
    }

    @Test
    void getSavedColors_ShouldReturnBadRequest_WhenInvalidUserId() {
        // Arrange
        String invalidUserId = "invalid-uuid";

        // Act
        ResponseEntity<?> response = savedColorController.getSavedColors(invalidUserId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao buscar cores salvas"));
        verify(savedColorService, never()).findByUserId(any());
    }

    @Test
    void getSavedColorsByTag_ShouldReturnFilteredColors_WhenSuccessful() {
        // Arrange
        String tag = "sala";
        List<SavedColorDTO> savedColors = List.of(savedColorDTO);
        when(savedColorService.findByUserIdAndEnvironmentTag(userId, tag)).thenReturn(savedColors);

        // Act
        ResponseEntity<?> response = savedColorController.getSavedColorsByTag(userId.toString(), tag);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedColors, response.getBody());
        verify(savedColorService).findByUserIdAndEnvironmentTag(userId, tag);
    }

    @Test
    void getSavedColorsByTag_ShouldReturnEmptyList_WhenNoColorsWithTag() {
        // Arrange
        String tag = "quarto";
        when(savedColorService.findByUserIdAndEnvironmentTag(userId, tag)).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = savedColorController.getSavedColorsByTag(userId.toString(), tag);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(savedColorService).findByUserIdAndEnvironmentTag(userId, tag);
    }

    @Test
    void getSavedColorsByTag_ShouldReturnBadRequest_WhenInvalidUserId() {
        // Arrange
        String invalidUserId = "invalid-uuid";
        String tag = "sala";

        // Act
        ResponseEntity<?> response = savedColorController.getSavedColorsByTag(invalidUserId, tag);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao buscar cores por tag"));
        verify(savedColorService, never()).findByUserIdAndEnvironmentTag(any(), anyString());
    }

    @Test
    void deleteSavedColor_ShouldReturnNoContent_WhenSuccessful() {
        // Arrange
        doNothing().when(savedColorService).deleteSavedColor(colorId, userId);

        // Act
        ResponseEntity<?> response = savedColorController.deleteSavedColor(colorId, userId.toString());

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(savedColorService).deleteSavedColor(colorId, userId);
    }

    @Test
    void deleteSavedColor_ShouldReturnBadRequest_WhenInvalidUserId() {
        // Arrange
        String invalidUserId = "invalid-uuid";

        // Act
        ResponseEntity<?> response = savedColorController.deleteSavedColor(colorId, invalidUserId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao deletar cor"));
        verify(savedColorService, never()).deleteSavedColor(any(), any());
    }

    @Test
    void deleteSavedColor_ShouldReturnBadRequest_WhenServiceThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Color not found"))
                .when(savedColorService).deleteSavedColor(colorId, userId);

        // Act
        ResponseEntity<?> response = savedColorController.deleteSavedColor(colorId, userId.toString());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao deletar cor"));
        verify(savedColorService).deleteSavedColor(colorId, userId);
    }

    @Test
    void saveColor_ShouldUseCorrectParameters() {
        // Arrange
        when(savedColorService.saveColor(any(SavedColorDTO.class), eq(userId))).thenReturn(savedColorDTO);

        // Act
        ResponseEntity<?> response = savedColorController.saveColor(savedColorDTO, userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(savedColorService).saveColor(
                argThat(dto ->
                        dto.getColorCode().equals("UB05") &&
                                dto.getColorName().equals("Azul Marinho") &&
                                dto.getEnvironmentTag().equals("sala") &&
                                dto.getHexCode().equals("#1F2A44")
                ),
                eq(userId)
        );
    }

    @Test
    void getSavedColorsByTag_ShouldUseCorrectTag() {
        // Arrange
        String tag = "cozinha";
        when(savedColorService.findByUserIdAndEnvironmentTag(userId, tag)).thenReturn(List.of(savedColorDTO));

        // Act
        ResponseEntity<?> response = savedColorController.getSavedColorsByTag(userId.toString(), tag);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(savedColorService).findByUserIdAndEnvironmentTag(userId, tag);
    }

    @Test
    void contextLoads() {
        assertNotNull(savedColorController);
        assertNotNull(savedColorService);
    }
}