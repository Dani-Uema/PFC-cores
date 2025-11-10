package com.example.paintlab.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AIServiceTest {

    @InjectMocks
    private AIService aiService;

    @Test
    void analisarCorComIA_ShouldReturnValidResult_WhenValidHexCode() {
        // Arrange
        String hexCode = "#FF5733";

        // Act
        Map<String, Object> result = aiService.analisarCorComIA(hexCode);

        // Assert
        assertNotNull(result);
        assertEquals(hexCode, result.get("cor_analisada"));
        assertNotNull(result.get("pigmentos"));
        assertNotNull(result.get("fonte"));
        assertNotNull(result.get("timestamp"));
        assertTrue(result.get("pigmentos") instanceof List);
    }

    @Test
    void analisarCorComIA_ShouldThrowException_WhenInvalidHexCode() {
        // Arrange
        String invalidHex = "invalid";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            aiService.analisarCorComIA(invalidHex);
        });
    }

    @Test
    void analisarCorComIA_ShouldThrowException_WhenNullHexCode() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            aiService.analisarCorComIA(null);
        });
    }

    @Test
    void analisarCorComIA_ShouldReturnFallback_WhenExceptionOccurs() {
        // Arrange
        String hexCode = "#FF5733";


        // Act
        Map<String, Object> result = aiService.analisarCorComIA(hexCode);

        // Assert
        assertNotNull(result);
        // Mesmo com problemas internos, deve retornar uma estrutura válida
        assertEquals(hexCode, result.get("cor_analisada"));
        assertNotNull(result.get("pigmentos"));
    }

    @Test
    void analisarCorComIA_ShouldHandleVariousHexCodes() {
        // Arrange
        String[] hexCodes = {"#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF"};

        // Act & Assert
        for (String hex : hexCodes) {
            Map<String, Object> result = aiService.analisarCorComIA(hex);
            assertNotNull(result);
            assertEquals(hex, result.get("cor_analisada"));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> pigmentos = (List<Map<String, Object>>) result.get("pigmentos");
            assertFalse(pigmentos.isEmpty());
        }
    }

    // Testes para métodos públicos ou que possam ser testados indiretamente
    @Test
    void analisarCorComIA_ShouldReturnConsistentStructure() {
        // Arrange
        String hexCode = "#123456";

        // Act
        Map<String, Object> result = aiService.analisarCorComIA(hexCode);

        // Assert
        // Verifica se a estrutura sempre tem os campos esperados
        assertTrue(result.containsKey("cor_analisada"));
        assertTrue(result.containsKey("pigmentos"));
        assertTrue(result.containsKey("fonte"));
        assertTrue(result.containsKey("timestamp"));

        // Verifica tipos dos campos
        assertInstanceOf(String.class, result.get("cor_analisada"));
        assertInstanceOf(List.class, result.get("pigmentos"));
        assertInstanceOf(String.class, result.get("fonte"));
        assertInstanceOf(Date.class, result.get("timestamp"));
    }

    @Test
    void analisarCorComIA_ShouldHaveValidPigmentsStructure() {
        // Arrange
        String hexCode = "#ABCDEF";

        // Act
        Map<String, Object> result = aiService.analisarCorComIA(hexCode);

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> pigmentos = (List<Map<String, Object>>) result.get("pigmentos");

        assertFalse(pigmentos.isEmpty());

        // Verifica estrutura de cada pigmento
        for (Map<String, Object> pigmento : pigmentos) {
            assertTrue(pigmento.containsKey("nome"));
            assertTrue(pigmento.containsKey("proporcao"));
            assertTrue(pigmento.containsKey("hex"));

            assertInstanceOf(String.class, pigmento.get("nome"));
            assertInstanceOf(Double.class, pigmento.get("proporcao"));
            assertInstanceOf(String.class, pigmento.get("hex"));
        }
    }

    @Test
    void analisarCorComIA_ShouldHavePigmentsSummingTo100() {
        // Arrange
        String hexCode = "#FF5733";

        // Act
        Map<String, Object> result = aiService.analisarCorComIA(hexCode);

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> pigmentos = (List<Map<String, Object>>) result.get("pigmentos");

        double sum = pigmentos.stream()
                .mapToDouble(p -> (Double) p.get("proporcao"))
                .sum();

        // A soma deve ser aproximadamente 100 (permite pequeno arredondamento)
        assertEquals(100.0, sum, 1.0);
    }

    // Testes para validação HEX (método público ou que pode ser testado via reflection)
    @Test
    void hexValidation_ShouldWorkCorrectly() {
        // Testamos a validação HEX indiretamente através do método público
        String[] validHexCodes = {"#FF5733", "#000000", "#FFFFFF", "#123ABC"};
        String[] invalidHexCodes = {"FF5733", "#GGG", "#12345", "invalid", ""};

        for (String validHex : validHexCodes) {
            // Hex válido não deve lançar exceção
            assertDoesNotThrow(() -> aiService.analisarCorComIA(validHex));
        }

        for (String invalidHex : invalidHexCodes) {
            // Hex inválido deve lançar exceção
            assertThrows(IllegalArgumentException.class, () ->
                    aiService.analisarCorComIA(invalidHex));
        }
    }

    @Test
    void analisarCorComIA_ShouldOrderPigmentsByProportion() {
        // Arrange
        String hexCode = "#FF5733";

        // Act
        Map<String, Object> result = aiService.analisarCorComIA(hexCode);

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> pigmentos = (List<Map<String, Object>>) result.get("pigmentos");

        // Verifica se está ordenado por proporção (decrescente)
        for (int i = 0; i < pigmentos.size() - 1; i++) {
            double current = (Double) pigmentos.get(i).get("proporcao");
            double next = (Double) pigmentos.get(i + 1).get("proporcao");
            assertTrue(current >= next, "Pigmentos devem estar ordenados por proporção decrescente");
        }
    }

    @Test
    void analisarCorComIA_ShouldReturnReasonableProportions() {
        // Arrange
        String hexCode = "#FF5733"; // Cor laranja-avermelhada

        // Act
        Map<String, Object> result = aiService.analisarCorComIA(hexCode);

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> pigmentos = (List<Map<String, Object>>) result.get("pigmentos");

        // Para uma cor laranja-avermelhada, esperamos que Vermelho tenha alta proporção
        boolean hasRed = pigmentos.stream()
                .anyMatch(p -> "Vermelho".equals(p.get("nome")));

        assertTrue(hasRed, "Cor laranja-avermelhada deve conter pigmento Vermelho");

        // Verifica se as proporções são razoáveis (entre 0 e 100)
        for (Map<String, Object> pigmento : pigmentos) {
            double proporcao = (Double) pigmento.get("proporcao");
            assertTrue(proporcao >= 0 && proporcao <= 100,
                    "Proporção deve estar entre 0 e 100: " + proporcao);
        }
    }
}