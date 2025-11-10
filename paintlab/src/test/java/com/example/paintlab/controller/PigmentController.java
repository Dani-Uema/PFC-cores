package com.example.paintlab.controller;

import com.example.paintlab.domain.pigments.Pigment;
import com.example.paintlab.dto.PigmentDTO;
import com.example.paintlab.service.PigmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PigmentControllerTest {

    @Mock
    private PigmentService pigmentService;

    @InjectMocks
    private PigmentController pigmentController;

    @Test
    void getAllPigments_ShouldReturnPigments() {
        // Arrange
        Pigment pigment = new Pigment();
        when(pigmentService.getAllPigments()).thenReturn(List.of(pigment));

        // Act
        ResponseEntity<List<Pigment>> response = pigmentController.getAllPigments();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(pigmentService).getAllPigments();
    }

    @Test
    void getPigmentById_ShouldReturnPigment() {
        // Arrange
        UUID pigmentId = UUID.randomUUID();
        Pigment pigment = new Pigment();
        when(pigmentService.getPigmentById(pigmentId)).thenReturn(pigment);

        // Act
        ResponseEntity<Pigment> response = pigmentController.getPigmentById(pigmentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(pigmentService).getPigmentById(pigmentId);
    }

    @Test
    void createPigment_ShouldReturnCreatedPigment() {
        // Arrange
        PigmentDTO pigmentDTO = new PigmentDTO();
        Pigment pigment = new Pigment();
        when(pigmentService.createPigment(any(PigmentDTO.class))).thenReturn(pigment);

        // Act
        ResponseEntity<Pigment> response = pigmentController.createPigment(pigmentDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(pigmentService).createPigment(any(PigmentDTO.class));
    }

    @Test
    void updatePigment_ShouldReturnUpdatedPigment() {
        // Arrange
        UUID pigmentId = UUID.randomUUID();
        PigmentDTO pigmentDTO = new PigmentDTO();
        Pigment pigment = new Pigment();
        when(pigmentService.updatePigment(eq(pigmentId), any(PigmentDTO.class))).thenReturn(pigment);

        // Act
        ResponseEntity<Pigment> response = pigmentController.updatePigment(pigmentId, pigmentDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(pigmentService).updatePigment(pigmentId, pigmentDTO);
    }

    @Test
    void deletePigment_ShouldReturnNoContent() {
        // Arrange
        UUID pigmentId = UUID.randomUUID();
        doNothing().when(pigmentService).deletePigment(pigmentId);

        // Act
        ResponseEntity<Void> response = pigmentController.deletePigment(pigmentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(pigmentService).deletePigment(pigmentId);
    }
}