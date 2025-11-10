package com.example.paintlab.service;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.composition.Composition;
import com.example.paintlab.domain.pigments.Pigment;
import com.example.paintlab.dto.*;
import com.example.paintlab.dto.color.ColorCreateDTO;
import com.example.paintlab.dto.color.ColorDTO;
import com.example.paintlab.dto.color.ColorUpdateDTO;
import com.example.paintlab.repositories.ColorRepository;
import com.example.paintlab.repositories.PigmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColorServiceTest {

    @Mock
    private ColorRepository colorRepository;

    @Mock
    private PigmentRepository pigmentRepository;

    @InjectMocks
    private ColorService colorService;

    private Color color;
    private UUID colorId;
    private Pigment pigment;
    private Composition composition;

    @BeforeEach
    void setUp() {
        colorId = UUID.randomUUID();
        UUID pigmentId = UUID.randomUUID();

        // Configura Pigment
        pigment = new Pigment();
        pigment.setId(pigmentId);
        pigment.setName("Ultramarine Blue");
        pigment.setHexCode("#1F2A44");

        // Configura Composition
        composition = new Composition();
        composition.setPercentage(100.0);
        Map<Pigment, Double> proportions = new HashMap<>();
        proportions.put(pigment, 70.0);
        composition.setPigmentProportions(proportions);
        composition.setPigments(List.of(pigment));

        // Configura Color
        color = new Color();
        color.setId(colorId);
        color.setName("Azul Ultramar");
        color.setBrand("Winsor & Newton");
        color.setColorCode("UB05");
        color.setHexCode("#1F2A44");
        color.setCompositions(List.of(composition));
    }

    @Test
    void searchColors_ShouldReturnAllColors_WhenSearchTermIsNull() {
        // Arrange
        when(colorRepository.findAll()).thenReturn(List.of(color));

        // Act
        List<Color> result = colorService.searchColors(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(colorRepository).findAll();
        verify(colorRepository, never()).findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrColorCodeContainingIgnoreCase(any(), any(), any());
    }

    @Test
    void searchColors_ShouldReturnAllColors_WhenSearchTermIsEmpty() {
        // Arrange
        when(colorRepository.findAll()).thenReturn(List.of(color));

        // Act
        List<Color> result = colorService.searchColors("");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(colorRepository).findAll();
        verify(colorRepository, never()).findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrColorCodeContainingIgnoreCase(any(), any(), any());
    }

    @Test
    void searchColors_ShouldReturnFilteredColors_WhenSearchTermProvided() {
        // Arrange
        String searchTerm = "azul";
        when(colorRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrColorCodeContainingIgnoreCase(
                searchTerm, searchTerm, searchTerm))
                .thenReturn(List.of(color));

        // Act
        List<Color> result = colorService.searchColors(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(colorRepository).findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrColorCodeContainingIgnoreCase(
                searchTerm, searchTerm, searchTerm);
    }

    @Test
    void getColorById_ShouldReturnColor_WhenColorExists() {
        // Arrange
        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));

        // Act
        Color result = colorService.getColorById(colorId);

        // Assert
        assertNotNull(result);
        assertEquals(colorId, result.getId());
        assertEquals("Azul Ultramar", result.getName());
        verify(colorRepository).findById(colorId);
    }

    @Test
    void getColorById_ShouldThrowException_WhenColorNotFound() {
        // Arrange
        when(colorRepository.findById(colorId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            colorService.getColorById(colorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Color not found"));
        verify(colorRepository).findById(colorId);
    }

    @Test
    void getPigmentsByColorId_ShouldReturnPigments_WhenColorHasCompositions() {
        // Arrange
        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));

        // Act
        List<PigmentCompositionDTO> result = colorService.getPigmentsByColorId(colorId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ultramarine Blue", result.get(0).getName());
        assertEquals(70.0, result.get(0).getProportion());
        verify(colorRepository).findById(colorId);
    }

    @Test
    void getPigmentsByColorId_ShouldReturnEmptyList_WhenNoCompositions() {
        // Arrange
        color.setCompositions(List.of());
        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));

        // Act
        List<PigmentCompositionDTO> result = colorService.getPigmentsByColorId(colorId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(colorRepository).findById(colorId);
    }

    @Test
    void getPigmentsByColorId_ShouldThrowException_WhenColorNotFound() {
        // Arrange
        when(colorRepository.findById(colorId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            colorService.getPigmentsByColorId(colorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(colorRepository).findById(colorId);
    }

    @Test
    void updateColorCompositions_ShouldUpdateCompositions_WhenColorExists() {
        // Arrange
        PigmentCompositionDTO pigmentDTO = new PigmentCompositionDTO();
        pigmentDTO.setName("New Pigment");
        pigmentDTO.setHexCode("#FF5733");
        pigmentDTO.setProportion(50.0);

        List<PigmentCompositionDTO> updatedPigments = List.of(pigmentDTO);

        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));
        when(pigmentRepository.findByNameIgnoreCase("New Pigment")).thenReturn(Optional.empty());
        when(colorRepository.save(any(Color.class))).thenReturn(color);

        // Act
        colorService.updateColorCompositions(colorId, updatedPigments);

        // Assert
        verify(colorRepository).findById(colorId);
        verify(pigmentRepository).findByNameIgnoreCase("New Pigment");
        verify(colorRepository).save(any(Color.class));
    }

    @Test
    void updateColorCompositions_ShouldThrowException_WhenColorNotFound() {
        // Arrange
        List<PigmentCompositionDTO> updatedPigments = List.of();
        when(colorRepository.findById(colorId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            colorService.updateColorCompositions(colorId, updatedPigments);
        });

        assertTrue(exception.getMessage().contains("Cor não encontrada"));
        verify(colorRepository).findById(colorId);
        verify(colorRepository, never()).save(any());
    }

    @Test
    void getAllColors_ShouldReturnAllColors() {
        // Arrange
        Color color2 = new Color();
        color2.setId(UUID.randomUUID());
        color2.setName("Vermelho Cadmium");

        when(colorRepository.findAll()).thenReturn(List.of(color, color2));

        // Act
        List<Color> result = colorService.getAllColors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(colorRepository).findAll();
    }

    @Test
    void createColor_ShouldCreateNewColor() {
        // Arrange
        ColorCreateDTO createDTO = new ColorCreateDTO();
        createDTO.setName("Nova Cor");
        createDTO.setBrand("Nova Marca");
        createDTO.setColorCode("NC001");
        createDTO.setHexCode("#FF5733");
        createDTO.setCompositions(List.of());

        when(colorRepository.save(any(Color.class))).thenReturn(color);

        // Act
        Color result = colorService.createColor(createDTO);

        // Assert
        assertNotNull(result);
        verify(colorRepository).save(any(Color.class));
    }

    @Test
    void createColor_ShouldCreateColorWithCompositions() {
        // Arrange
        UUID pigmentId = UUID.randomUUID();

        CompositionDTO compDTO = new CompositionDTO();
        compDTO.setPercentage(100.0);
        compDTO.setPigmentProportions(Map.of(pigmentId, 70.0));

        ColorCreateDTO createDTO = new ColorCreateDTO();
        createDTO.setName("Nova Cor");
        createDTO.setCompositions(List.of(compDTO));

        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.of(pigment));
        when(colorRepository.save(any(Color.class))).thenReturn(color);

        // Act
        Color result = colorService.createColor(createDTO);

        // Assert
        assertNotNull(result);
        verify(pigmentRepository).findById(pigmentId);
        verify(colorRepository).save(any(Color.class));
    }

    @Test
    void updateColor_ShouldUpdateExistingColor() {
        // Arrange
        ColorUpdateDTO updateDTO = new ColorUpdateDTO();
        updateDTO.setName("Nome Atualizado");
        updateDTO.setBrand("Marca Atualizada");

        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));
        when(colorRepository.save(any(Color.class))).thenReturn(color);

        // Act
        ColorDTO result = colorService.updateColor(colorId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(colorRepository).findById(colorId);
        verify(colorRepository).save(any(Color.class));
    }

    @Test
    void updateColor_ShouldThrowException_WhenColorNotFound() {
        // Arrange
        ColorUpdateDTO updateDTO = new ColorUpdateDTO();
        when(colorRepository.findById(colorId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            colorService.updateColor(colorId, updateDTO);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(colorRepository).findById(colorId);
        verify(colorRepository, never()).save(any());
    }

    @Test
    void deleteColor_ShouldDeleteExistingColor() {
        // Arrange
        when(colorRepository.findById(colorId)).thenReturn(Optional.of(color));
        doNothing().when(colorRepository).delete(color);

        // Act
        colorService.deleteColor(colorId);

        // Assert
        verify(colorRepository).findById(colorId);
        verify(colorRepository).delete(color);
    }

    @Test
    void deleteColor_ShouldThrowException_WhenColorNotFound() {
        // Arrange
        when(colorRepository.findById(colorId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            colorService.deleteColor(colorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(colorRepository).findById(colorId);
        verify(colorRepository, never()).delete(any());
    }

    @Test
    void getPigmentsByColorName_ShouldReturnPigments_WhenColorExists() {
        // Arrange
        String colorName = "Azul Ultramar";
        when(colorRepository.findByNameIgnoreCase(colorName)).thenReturn(Optional.of(color));

        // Act
        List<PigmentCompositionDTO> result = colorService.getPigmentsByColorName(colorName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(colorRepository).findByNameIgnoreCase(colorName);
    }

    @Test
    void getPigmentsByColorName_ShouldReturnEmptyList_WhenColorNotFound() {
        // Arrange
        String colorName = "Cor Inexistente";
        when(colorRepository.findByNameIgnoreCase(colorName)).thenReturn(Optional.empty());

        // Act
        List<PigmentCompositionDTO> result = colorService.getPigmentsByColorName(colorName);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(colorRepository).findByNameIgnoreCase(colorName);
    }
}
