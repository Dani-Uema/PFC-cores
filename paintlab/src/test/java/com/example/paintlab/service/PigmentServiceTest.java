package com.example.paintlab.service;

import com.example.paintlab.domain.pigments.Pigment;
import com.example.paintlab.dto.PigmentDTO;
import com.example.paintlab.repositories.PigmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PigmentServiceTest {

    @Mock
    private PigmentRepository pigmentRepository;

    @InjectMocks
    private PigmentService pigmentService;

    private UUID pigmentId;
    private Pigment pigment;
    private PigmentDTO pigmentDTO;

    @BeforeEach
    void setUp() {
        pigmentId = UUID.randomUUID();

        pigment = new Pigment("Ultramarine Blue", "#1F2A44");
        pigment.setId(pigmentId);

        pigmentDTO = new PigmentDTO();
        pigmentDTO.setName("New Pigment");
    }

    @Test
    void getAllPigments_ShouldReturnAllPigments() {
        // Arrange
        Pigment pigment2 = new Pigment("Cadmium Red", "#FF0000");
        pigment2.setId(UUID.randomUUID());

        when(pigmentRepository.findAll()).thenReturn(List.of(pigment, pigment2));

        // Act
        List<Pigment> result = pigmentService.getAllPigments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pigmentRepository).findAll();
    }

    @Test
    void getAllPigments_ShouldReturnEmptyList_WhenNoPigments() {
        // Arrange
        when(pigmentRepository.findAll()).thenReturn(List.of());

        // Act
        List<Pigment> result = pigmentService.getAllPigments();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pigmentRepository).findAll();
    }

    @Test
    void getPigmentById_ShouldReturnPigment_WhenPigmentExists() {
        // Arrange
        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.of(pigment));

        // Act
        Pigment result = pigmentService.getPigmentById(pigmentId);

        // Assert
        assertNotNull(result);
        assertEquals(pigmentId, result.getId());
        assertEquals("Ultramarine Blue", result.getName());
        assertEquals("#1F2A44", result.getHexCode());
        verify(pigmentRepository).findById(pigmentId);
    }

    @Test
    void getPigmentById_ShouldThrowException_WhenPigmentNotFound() {
        // Arrange
        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pigmentService.getPigmentById(pigmentId);
        });

        assertTrue(exception.getMessage().contains("Pigment not found"));
        verify(pigmentRepository).findById(pigmentId);
    }

    @Test
    void createPigment_ShouldCreateNewPigment() {
        // Arrange
        when(pigmentRepository.save(any(Pigment.class))).thenReturn(pigment);

        // Act
        Pigment result = pigmentService.createPigment(pigmentDTO);

        // Assert
        assertNotNull(result);
        verify(pigmentRepository).save(any(Pigment.class));
    }

    @Test
    void createPigment_ShouldSetCorrectProperties() {
        // Arrange
        when(pigmentRepository.save(any(Pigment.class))).thenAnswer(invocation -> {
            Pigment savedPigment = invocation.getArgument(0);
            savedPigment.setId(pigmentId);
            return savedPigment;
        });

        // Act
        Pigment result = pigmentService.createPigment(pigmentDTO);

        // Assert
        assertNotNull(result);
        verify(pigmentRepository).save(argThat(p ->
                        p.getName().equals("New Pigment")
                // hexCode não é definido pelo DTO, pode ser null ou valor padrão
        ));
    }

    @Test
    void updatePigment_ShouldUpdateExistingPigment() {
        // Arrange
        PigmentDTO updateDTO = new PigmentDTO();
        updateDTO.setName("Updated Pigment");
        // Não tem hexCode no DTO

        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.of(pigment));
        when(pigmentRepository.save(any(Pigment.class))).thenReturn(pigment);

        // Act
        Pigment result = pigmentService.updatePigment(pigmentId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(pigmentRepository).findById(pigmentId);
        verify(pigmentRepository).save(pigment);
    }

    @Test
    void updatePigment_ShouldUpdateOnlyName() {
        // Arrange
        PigmentDTO updateDTO = new PigmentDTO();
        updateDTO.setName("Partially Updated");

        Pigment existingPigment = new Pigment("Original Name", "#000000");
        existingPigment.setId(pigmentId);

        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.of(existingPigment));
        when(pigmentRepository.save(any(Pigment.class))).thenReturn(existingPigment);

        // Act
        Pigment result = pigmentService.updatePigment(pigmentId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(pigmentRepository).save(argThat(p ->
                p.getName().equals("Partially Updated") &&
                        p.getHexCode().equals("#000000") // Mantém o hexCode original (não é atualizado pelo DTO)
        ));
    }

    @Test
    void updatePigment_ShouldIgnoreProportion() {
        // Arrange
        PigmentDTO updateDTO = new PigmentDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setProportion(50.0); // Proportion deve ser ignorado no update

        Pigment existingPigment = new Pigment("Original Name", "#000000");
        existingPigment.setId(pigmentId);

        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.of(existingPigment));
        when(pigmentRepository.save(any(Pigment.class))).thenReturn(existingPigment);

        // Act
        Pigment result = pigmentService.updatePigment(pigmentId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(pigmentRepository).save(argThat(p ->
                p.getName().equals("Updated Name") &&
                        p.getHexCode().equals("#000000") // Mantém o hexCode original
        ));
    }

    @Test
    void updatePigment_ShouldThrowException_WhenPigmentNotFound() {
        // Arrange
        PigmentDTO updateDTO = new PigmentDTO();
        updateDTO.setName("Updated Name");

        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pigmentService.updatePigment(pigmentId, updateDTO);
        });

        assertTrue(exception.getMessage().contains("Pigment not found"));
        verify(pigmentRepository).findById(pigmentId);
        verify(pigmentRepository, never()).save(any());
    }

    @Test
    void deletePigment_ShouldDeleteExistingPigment() {
        // Arrange
        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.of(pigment));
        doNothing().when(pigmentRepository).delete(pigment);

        // Act
        pigmentService.deletePigment(pigmentId);

        // Assert
        verify(pigmentRepository).findById(pigmentId);
        verify(pigmentRepository).delete(pigment);
    }

    @Test
    void deletePigment_ShouldThrowException_WhenPigmentNotFound() {
        // Arrange
        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pigmentService.deletePigment(pigmentId);
        });

        assertTrue(exception.getMessage().contains("Pigment not found"));
        verify(pigmentRepository).findById(pigmentId);
        verify(pigmentRepository, never()).delete(any());
    }

    @Test
    void initializeBasicPigments_ShouldNotCreatePigments_WhenAlreadyExist() {
        // Arrange
        when(pigmentRepository.count()).thenReturn(10L); // Já existem pigmentos

        // Act - O método é chamado no construtor, então recriamos o serviço
        PigmentService newService = new PigmentService(pigmentRepository);

        // Assert
        verify(pigmentRepository).count();
        verify(pigmentRepository, never()).saveAll(any());
    }

    @Test
    void initializeBasicPigments_ShouldCreatePigments_WhenNoneExist() {
        // Arrange
        when(pigmentRepository.count()).thenReturn(0L);
        when(pigmentRepository.saveAll(any())).thenReturn(List.of(pigment));

        PigmentService newService = new PigmentService(pigmentRepository);

        // Assert
        verify(pigmentRepository).count();
        verify(pigmentRepository).saveAll(any());
    }

    @Test
    void createPigment_ShouldHandleNullFields() {
        // Arrange
        PigmentDTO nullDTO = new PigmentDTO(); // Todos os campos são null
        when(pigmentRepository.save(any(Pigment.class))).thenReturn(pigment);

        // Act
        Pigment result = pigmentService.createPigment(nullDTO);

        // Assert
        assertNotNull(result);
        verify(pigmentRepository).save(any(Pigment.class));
    }

    @Test
    void updatePigment_ShouldHandleNullUpdateDTO() {
        // Arrange
        PigmentDTO nullDTO = new PigmentDTO(); // Todos os campos são null

        Pigment existingPigment = new Pigment("Original Name", "#000000");
        existingPigment.setId(pigmentId);

        when(pigmentRepository.findById(pigmentId)).thenReturn(Optional.of(existingPigment));
        when(pigmentRepository.save(any(Pigment.class))).thenReturn(existingPigment);

        // Act
        Pigment result = pigmentService.updatePigment(pigmentId, nullDTO);

        // Assert
        assertNotNull(result);
        // O pigmento deve manter seus valores originais
        verify(pigmentRepository).save(argThat(p ->
                p.getName().equals("Original Name") && // Mantém o nome original
                        p.getHexCode().equals("#000000")       // Mantém o hexCode original
        ));
    }

    @Test
    void pigmentEqualsAndHashCode_ShouldWorkCorrectly() {
        // Arrange
        Pigment pigment1 = new Pigment("Test", "#FFFFFF");
        pigment1.setId(pigmentId);

        Pigment pigment2 = new Pigment("Different", "#000000");
        pigment2.setId(pigmentId); // Mesmo ID

        Pigment pigment3 = new Pigment("Test", "#FFFFFF");
        pigment3.setId(UUID.randomUUID()); // ID diferente

        // Act & Assert
        assertEquals(pigment1, pigment2); // Mesmo ID
        assertNotEquals(pigment1, pigment3); // IDs diferentes
        assertEquals(pigment1.hashCode(), pigment2.hashCode()); // Hash codes iguais para mesmo ID
    }

    @Test
    void contextLoads() {
        assertNotNull(pigmentService);
        assertNotNull(pigmentRepository);
    }
}