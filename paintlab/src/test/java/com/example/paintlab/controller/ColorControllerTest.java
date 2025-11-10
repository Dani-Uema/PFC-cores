package com.example.paintlab.controller;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.dto.color.ColorCreateDTO;
import com.example.paintlab.dto.color.ColorDTO;
import com.example.paintlab.dto.color.ColorUpdateDTO;
import com.example.paintlab.dto.PigmentCompositionDTO;
import com.example.paintlab.dto.CompositionDTO;
import com.example.paintlab.dto.PigmentDTO;
import com.example.paintlab.service.ColorService;
import jakarta.persistence.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColorControllerTest {

    @Mock
    private ColorService colorService;

    @InjectMocks
    private ColorController colorController;

    private Color color;
    private UUID colorId;
    private ColorCreateDTO colorCreateDTO;
    private ColorUpdateDTO colorUpdateDTO;
    private PigmentCompositionDTO pigmentCompositionDTO;

    @BeforeEach
    void setUp() {
        colorId = UUID.randomUUID();

        // Configura a entidade Color
        color = new Color();
        color.setId(colorId);
        color.setName("Azul Ultramar");
        color.setBrand("Winsor & Newton");
        color.setColorCode("UB05");
        color.setHexCode("#1F2A44");
        color.setCompositions(List.of());

        // Configura ColorCreateDTO
        colorCreateDTO = new ColorCreateDTO();
        colorCreateDTO.setName("Azul Ultramar");
        colorCreateDTO.setBrand("Winsor & Newton");
        colorCreateDTO.setColorCode("UB05");
        colorCreateDTO.setHexCode("#1F2A44");
        colorCreateDTO.setCompositions(List.of());

        // Configura ColorUpdateDTO
        colorUpdateDTO = new ColorUpdateDTO();
        colorUpdateDTO.setName("Azul Ultramar Premium");
        colorUpdateDTO.setBrand("Winsor & Newton");
        colorUpdateDTO.setColorCode("UB05-P");
        colorUpdateDTO.setHexCode("#1F2A44");
        colorUpdateDTO.setCompositions(List.of());

        // Configura PigmentCompositionDTO
        pigmentCompositionDTO = new PigmentCompositionDTO();
        pigmentCompositionDTO.setName("Ultramarine Blue");
        pigmentCompositionDTO.setHexCode("#1F2A44");
        pigmentCompositionDTO.setProportion(70.5);
    }

    @Test
    void searchColors_ShouldReturnColors_WhenQueryIsProvided() {
        // Arrange
        String query = "azul";
        when(colorService.searchColors(query)).thenReturn(List.of(color));

        // Act
        ResponseEntity<List<Color>> response = colorController.searchColors(query);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Azul Ultramar", response.getBody().get(0).getName());
        verify(colorService).searchColors(query);
    }

    @Test
    void getColorById_ShouldReturnColor_WhenColorExists() {
        // Arrange
        when(colorService.getColorById(colorId)).thenReturn(color);

        // Act
        ResponseEntity<Color> response = colorController.getColorById(colorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(colorId, response.getBody().getId());
        assertEquals("Azul Ultramar", response.getBody().getName());
        verify(colorService).getColorById(colorId);
    }

    @Test
    void getPigmentsByColorId_ShouldReturnPigments_WhenColorHasPigments() {
        // Arrange
        List<PigmentCompositionDTO> pigments = List.of(pigmentCompositionDTO);
        when(colorService.getPigmentsByColorId(colorId)).thenReturn(pigments);

        // Act
        ResponseEntity<List<PigmentCompositionDTO>> response = colorController.getPigmentsByColorId(colorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Ultramarine Blue", response.getBody().get(0).getName());
        assertEquals(70.5, response.getBody().get(0).getProportion());
        verify(colorService).getPigmentsByColorId(colorId);
    }

    @Test
    void getPigmentsByColorId_ShouldReturnNotFound_WhenNoPigmentsFound() {
        // Arrange
        when(colorService.getPigmentsByColorId(colorId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<PigmentCompositionDTO>> response = colorController.getPigmentsByColorId(colorId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(colorService).getPigmentsByColorId(colorId);
    }

    @Test
    void getAllColors_ShouldReturnAllColors() {
        // Arrange
        Color color2 = new Color();
        color2.setId(UUID.randomUUID());
        color2.setName("Vermelho Cadmium");

        when(colorService.getAllColors()).thenReturn(List.of(color, color2));

        // Act
        ResponseEntity<List<Color>> response = colorController.getAllColors();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(colorService).getAllColors();
    }

    @Test
    void createColor_ShouldReturnCreatedColor() {
        // Arrange
        when(colorService.createColor(any(ColorCreateDTO.class))).thenReturn(color);

        // Act
        ResponseEntity<Color> response = colorController.createColor(colorCreateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(colorId, response.getBody().getId());
        verify(colorService).createColor(any(ColorCreateDTO.class));
    }

    @Test
    void updateColor_ShouldReturnUpdatedColor() {
        // Arrange
        // Cria um ColorDTO usando o construtor que recebe Color
        ColorDTO colorDTO = new ColorDTO(color);
        when(colorService.updateColor(eq(colorId), any(ColorUpdateDTO.class))).thenReturn(colorDTO);

        // Act
        ResponseEntity<ColorDTO> response = colorController.updateColor(colorId, colorUpdateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Azul Ultramar", response.getBody().getName());
        verify(colorService).updateColor(colorId, colorUpdateDTO);
    }

    @Test
    void updateColorCompositions_ShouldReturnOk_WhenUpdateIsSuccessful() {
        // Arrange
        List<PigmentCompositionDTO> updatedPigments = List.of(pigmentCompositionDTO);
        doNothing().when(colorService).updateColorCompositions(colorId, updatedPigments);

        // Act
        ResponseEntity<?> response = colorController.updateColorCompositions(colorId, updatedPigments);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Composição atualizada com sucesso!", response.getBody());
        verify(colorService).updateColorCompositions(colorId, updatedPigments);
    }

    @Test
    void updateColorCompositions_ShouldReturnNotFound_WhenColorDoesNotExist() {
        // Arrange
        List<PigmentCompositionDTO> updatedPigments = List.of(pigmentCompositionDTO);
        String errorMessage = "Cor não encontrada";
        doThrow(new EntityNotFoundException(errorMessage))
                .when(colorService).updateColorCompositions(colorId, updatedPigments);

        // Act
        ResponseEntity<?> response = colorController.updateColorCompositions(colorId, updatedPigments);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(colorService).updateColorCompositions(colorId, updatedPigments);
    }

    @Test
    void updateColorCompositions_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() {
        // Arrange
        List<PigmentCompositionDTO> updatedPigments = List.of(pigmentCompositionDTO);
        doThrow(new RuntimeException("Erro inesperado"))
                .when(colorService).updateColorCompositions(colorId, updatedPigments);

        // Act
        ResponseEntity<?> response = colorController.updateColorCompositions(colorId, updatedPigments);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao atualizar composição.", response.getBody());
        verify(colorService).updateColorCompositions(colorId, updatedPigments);
    }

    @Test
    void deleteColor_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(colorService).deleteColor(colorId);

        // Act
        ResponseEntity<Void> response = colorController.deleteColor(colorId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(colorService).deleteColor(colorId);
    }

    @Test
    void getAllColors_ShouldReturnEmptyList_WhenNoColorsExist() {
        // Arrange
        when(colorService.getAllColors()).thenReturn(List.of());

        // Act
        ResponseEntity<List<Color>> response = colorController.getAllColors();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(colorService).getAllColors();
    }

    @Test
    void createColor_ShouldUseCorrectDTO() {
        // Arrange
        when(colorService.createColor(any(ColorCreateDTO.class))).thenReturn(color);

        // Act
        ResponseEntity<Color> response = colorController.createColor(colorCreateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(colorService).createColor(argThat(dto ->
                dto.getName().equals("Azul Ultramar") &&
                        dto.getBrand().equals("Winsor & Newton") &&
                        dto.getColorCode().equals("UB05")
        ));
    }
}