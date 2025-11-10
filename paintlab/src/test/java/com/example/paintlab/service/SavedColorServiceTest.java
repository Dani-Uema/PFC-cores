package com.example.paintlab.service;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.savedcolor.SavedColor;
import com.example.paintlab.domain.user.User;
import com.example.paintlab.dto.SavedColorDTO;
import com.example.paintlab.repositories.ColorRepository;
import com.example.paintlab.repositories.SavedColorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavedColorServiceTest {

    @Mock
    private SavedColorRepository savedColorRepository;

    @Mock
    private UserService userService;

    @Mock
    private ColorRepository colorRepository;

    @InjectMocks
    private SavedColorService savedColorService;

    private UUID userId;
    private UUID savedColorId;
    private User user;
    private SavedColor savedColor;
    private SavedColorDTO savedColorDTO;
    private Color color;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        savedColorId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        savedColor = new SavedColor();
        savedColor.setId(savedColorId);
        savedColor.setUser(user);
        savedColor.setColorCode("UB05");
        savedColor.setColorName("Azul Ultramar");
        savedColor.setEnvironmentTag("sala");
        savedColor.setNotes("Cor para parede da sala");
        savedColor.setSavedDate(LocalDateTime.now());

        savedColorDTO = new SavedColorDTO();
        savedColorDTO.setColorCode("UB05");
        savedColorDTO.setColorName("Azul Ultramar");
        savedColorDTO.setEnvironmentTag("sala");
        savedColorDTO.setNotes("Cor para parede da sala");

        color = new Color();
        color.setColorCode("UB05");
        color.setHexCode("#1F2A44");
    }

    @Test
    void saveColor_ShouldSaveColor_WhenValidData() {
        // Arrange
        when(userService.findById(userId)).thenReturn(user);
        when(savedColorRepository.findByUserIdAndColorCodeAndEnvironmentTag(
                userId, "UB05", "sala")).thenReturn(List.of());
        when(savedColorRepository.save(any(SavedColor.class))).thenReturn(savedColor);
        when(colorRepository.findByColorCode("UB05")).thenReturn(color);

        // Act
        SavedColorDTO result = savedColorService.saveColor(savedColorDTO, userId);

        // Assert
        assertNotNull(result);
        assertEquals("UB05", result.getColorCode());
        assertEquals("Azul Ultramar", result.getColorName());
        assertEquals("#1F2A44", result.getHexCode()); // Hex code da cor original
        verify(userService).findById(userId);
        verify(savedColorRepository).findByUserIdAndColorCodeAndEnvironmentTag(userId, "UB05", "sala");
        verify(savedColorRepository).save(any(SavedColor.class));
    }

    @Test
    void saveColor_ShouldThrowException_WhenColorAlreadyExists() {
        // Arrange
        when(userService.findById(userId)).thenReturn(user);
        when(savedColorRepository.findByUserIdAndColorCodeAndEnvironmentTag(
                userId, "UB05", "sala")).thenReturn(List.of(savedColor));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            savedColorService.saveColor(savedColorDTO, userId);
        });

        assertTrue(exception.getMessage().contains("Esta cor já foi salva com a tag"));
        verify(userService).findById(userId);
        verify(savedColorRepository).findByUserIdAndColorCodeAndEnvironmentTag(userId, "UB05", "sala");
        verify(savedColorRepository, never()).save(any());
    }

    @Test
    void saveColor_ShouldGenerateHexCode_WhenOriginalColorNotFound() {
        // Arrange
        when(userService.findById(userId)).thenReturn(user);
        when(savedColorRepository.findByUserIdAndColorCodeAndEnvironmentTag(
                userId, "UB05", "sala")).thenReturn(List.of());
        when(savedColorRepository.save(any(SavedColor.class))).thenReturn(savedColor);
        when(colorRepository.findByColorCode("UB05")).thenReturn(null); // Cor original não encontrada

        // Act
        SavedColorDTO result = savedColorService.saveColor(savedColorDTO, userId);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getHexCode()); // Deve gerar um hex code a partir do nome
        assertTrue(result.getHexCode().startsWith("#"));
        verify(colorRepository).findByColorCode("UB05");
    }

    @Test
    void findByUserId_ShouldReturnSavedColors() {
        // Arrange
        when(savedColorRepository.findByUserIdOrderBySavedDateDesc(userId))
                .thenReturn(List.of(savedColor));
        when(colorRepository.findByColorCode("UB05")).thenReturn(color);

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Azul Ultramar", result.get(0).getColorName());
        verify(savedColorRepository).findByUserIdOrderBySavedDateDesc(userId);
    }

    @Test
    void findByUserId_ShouldReturnEmptyList_WhenNoSavedColors() {
        // Arrange
        when(savedColorRepository.findByUserIdOrderBySavedDateDesc(userId))
                .thenReturn(List.of());

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(savedColorRepository).findByUserIdOrderBySavedDateDesc(userId);
    }

    @Test
    void findByUserIdAndEnvironmentTag_ShouldReturnFilteredColors() {
        // Arrange
        String tag = "sala";
        when(savedColorRepository.findByUserIdAndEnvironmentTagContaining(userId, tag))
                .thenReturn(List.of(savedColor));
        when(colorRepository.findByColorCode("UB05")).thenReturn(color);

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserIdAndEnvironmentTag(userId, tag);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("sala", result.get(0).getEnvironmentTag());
        verify(savedColorRepository).findByUserIdAndEnvironmentTagContaining(userId, tag);
    }

    @Test
    void findByUserIdAndEnvironmentTag_ShouldReturnEmptyList_WhenNoMatches() {
        // Arrange
        String tag = "cozinha";
        when(savedColorRepository.findByUserIdAndEnvironmentTagContaining(userId, tag))
                .thenReturn(List.of());

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserIdAndEnvironmentTag(userId, tag);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(savedColorRepository).findByUserIdAndEnvironmentTagContaining(userId, tag);
    }

    @Test
    void deleteSavedColor_ShouldDeleteColor_WhenExistsAndBelongsToUser() {
        // Arrange
        when(savedColorRepository.findByIdAndUserId(savedColorId, userId))
                .thenReturn(Optional.of(savedColor));
        doNothing().when(savedColorRepository).deleteByIdAndUserId(savedColorId, userId);

        // Act
        savedColorService.deleteSavedColor(savedColorId, userId);

        // Assert
        verify(savedColorRepository).findByIdAndUserId(savedColorId, userId);
        verify(savedColorRepository).deleteByIdAndUserId(savedColorId, userId);
    }

    @Test
    void deleteSavedColor_ShouldThrowException_WhenColorNotFound() {
        // Arrange
        when(savedColorRepository.findByIdAndUserId(savedColorId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            savedColorService.deleteSavedColor(savedColorId, userId);
        });

        assertTrue(exception.getMessage().contains("Cor salva não encontrada ou não pertence ao usuário"));
        verify(savedColorRepository).findByIdAndUserId(savedColorId, userId);
        verify(savedColorRepository, never()).deleteByIdAndUserId(any(), any());
    }

    @Test
    void convertToDTO_ShouldSetHexCodeFromOriginalColor() {
        // Arrange
        when(savedColorRepository.findByUserIdOrderBySavedDateDesc(userId))
                .thenReturn(List.of(savedColor));
        when(colorRepository.findByColorCode("UB05")).thenReturn(color);

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("#1F2A44", result.get(0).getHexCode());
    }

    @Test
    void convertToDTO_ShouldGenerateHexCode_WhenOriginalColorHasNoHexCode() {
        // Arrange
        Color colorWithoutHex = new Color();
        colorWithoutHex.setColorCode("UB05");
        colorWithoutHex.setHexCode(null);

        when(savedColorRepository.findByUserIdOrderBySavedDateDesc(userId))
                .thenReturn(List.of(savedColor));
        when(colorRepository.findByColorCode("UB05")).thenReturn(colorWithoutHex);

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getHexCode());
        assertTrue(result.get(0).getHexCode().startsWith("#"));
    }

    @Test
    void convertToDTO_ShouldGenerateHexCode_WhenColorRepositoryThrowsException() {
        // Arrange
        when(savedColorRepository.findByUserIdOrderBySavedDateDesc(userId))
                .thenReturn(List.of(savedColor));
        when(colorRepository.findByColorCode("UB05")).thenThrow(new RuntimeException("Database error"));

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getHexCode());
        assertTrue(result.get(0).getHexCode().startsWith("#"));
    }

    @Test
    void shouldGenerateConsistentHexCodes_ForSameColorNames() {
        // Arrange
        SavedColor color1 = new SavedColor();
        color1.setId(UUID.randomUUID());
        color1.setColorCode("TEST1");
        color1.setColorName("Test Color");
        color1.setEnvironmentTag("test");

        SavedColor color2 = new SavedColor();
        color2.setId(UUID.randomUUID());
        color2.setColorCode("TEST2");
        color2.setColorName("Test Color"); // Mesmo nome
        color2.setEnvironmentTag("test");

        when(savedColorRepository.findByUserIdOrderBySavedDateDesc(userId))
                .thenReturn(List.of(color1, color2));
        when(colorRepository.findByColorCode(anyString())).thenReturn(null); // Força geração de hex code

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        // Cores com o mesmo nome devem ter o mesmo hex code gerado
        assertEquals(result.get(0).getHexCode(), result.get(1).getHexCode());
    }

    @Test
    void saveColor_ShouldUseAllDTOFields() {
        // Arrange
        savedColorDTO.setNotes("Notas especiais");
        when(userService.findById(userId)).thenReturn(user);
        when(savedColorRepository.findByUserIdAndColorCodeAndEnvironmentTag(
                userId, "UB05", "sala")).thenReturn(List.of());
        when(savedColorRepository.save(any(SavedColor.class))).thenReturn(savedColor);
        when(colorRepository.findByColorCode("UB05")).thenReturn(color);

        // Act
        SavedColorDTO result = savedColorService.saveColor(savedColorDTO, userId);

        // Assert
        assertNotNull(result);
        assertEquals("Notas especiais", result.getNotes());
        verify(savedColorRepository).save(argThat(saved ->
                saved.getColorCode().equals("UB05") &&
                        saved.getColorName().equals("Azul Ultramar") &&
                        saved.getEnvironmentTag().equals("sala") &&
                        saved.getNotes().equals("Notas especiais") &&
                        saved.getUser().equals(user)
        ));
    }

    @Test
    void findByUserId_ShouldOrderBySavedDateDesc() {
        // Arrange
        SavedColor olderColor = new SavedColor();
        olderColor.setId(UUID.randomUUID());
        olderColor.setSavedDate(LocalDateTime.now().minusDays(1));

        SavedColor newerColor = new SavedColor();
        newerColor.setId(UUID.randomUUID());
        newerColor.setSavedDate(LocalDateTime.now());

        when(savedColorRepository.findByUserIdOrderBySavedDateDesc(userId))
                .thenReturn(List.of(newerColor, olderColor)); // Ordenado: mais recente primeiro
        when(colorRepository.findByColorCode(any())).thenReturn(color);

        // Act
        List<SavedColorDTO> result = savedColorService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(savedColorRepository).findByUserIdOrderBySavedDateDesc(userId);
    }

    @Test
    void contextLoads() {
        assertNotNull(savedColorService);
        assertNotNull(savedColorRepository);
        assertNotNull(userService);
        assertNotNull(colorRepository);
    }
}