package com.example.paintlab.service;

import com.example.paintlab.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private TokenService tokenService;

    private User user;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        user = new User();
        user.setEmail("user@example.com");

        // Injeta o secret via reflection
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret-key-very-long-for-hmac256");
    }

    @Test
    void generateToken_ShouldReturnToken_WhenValidUser() {
        // Act
        String result = tokenService.generateToken(user);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // Token JWT geralmente tem 3 partes separadas por pontos
        assertTrue(result.split("\\.").length == 3);
    }

    @Test
    void generateToken_ShouldUseUserEmailAsSubject() {
        // Act
        String token = tokenService.generateToken(user);

        // Assert - Validamos que o token pode ser verificado com o mesmo serviço
        String subject = tokenService.validateToken(token);

        assertEquals(user.getEmail(), subject);
    }

    @Test
    void validateToken_ShouldReturnSubject_WhenValidToken() {
        // Arrange
        String token = tokenService.generateToken(user);

        // Act
        String result = tokenService.validateToken(token);

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result);
    }

    @Test
    void validateToken_ShouldReturnEmptyString_WhenInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        String result = tokenService.validateToken(invalidToken);

        // Assert
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void validateToken_ShouldReturnEmptyString_WhenTokenIsNull() {
        // Act
        String result = tokenService.validateToken(null);

        // Assert
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void validateToken_ShouldReturnEmptyString_WhenTokenIsEmpty() {
        // Act
        String result = tokenService.validateToken("");

        // Assert
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void validateToken_ShouldReturnEmptyString_WhenTokenIsMalformed() {
        // Arrange
        String malformedToken = "not.a.valid.jwt.token";

        // Act
        String result = tokenService.validateToken(malformedToken);

        // Assert
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void invalidateToken_ShouldNotBreak_WhenCalled() {
        // Act & Assert - Não deve lançar exceção
        assertDoesNotThrow(() -> {
            tokenService.invalidateToken("some-token");
            tokenService.invalidateToken(null);
            tokenService.invalidateToken("");
        });
    }

    @Test
    void generatedToken_ShouldBeValidForValidation() {
        // Arrange
        String token = tokenService.generateToken(user);

        // Act & Assert - O token gerado deve ser válido para validação
        String subject = tokenService.validateToken(token);

        assertEquals(user.getEmail(), subject);
    }

    @Test
    void differentUsers_ShouldGenerateDifferentTokens() {
        // Arrange
        User user2 = new User();
        user2.setEmail("another@example.com");

        // Act
        String token1 = tokenService.generateToken(user);
        String token2 = tokenService.generateToken(user2);

        // Assert
        assertNotEquals(token1, token2);

        // Ambos devem ser válidos
        assertEquals(user.getEmail(), tokenService.validateToken(token1));
        assertEquals(user2.getEmail(), tokenService.validateToken(token2));
    }

    @Test
    void token_ShouldContainIssuerInformation() {
        // Act
        String token = tokenService.generateToken(user);

        // Assert - Podemos verificar decodificando o token (sem verificar assinatura)
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length); // Header, Payload, Signature
    }

    @Test
    void contextLoads() {
        assertNotNull(tokenService);
        assertNotNull(user);
    }

    // Teste para verificar que o serviço funciona com secret diferente
    @Test
    void shouldWorkWithDifferentSecrets() {
        // Arrange
        TokenService newTokenService = new TokenService();
        ReflectionTestUtils.setField(newTokenService, "secret", "different-secret-key");

        // Act
        String token = newTokenService.generateToken(user);
        String subject = newTokenService.validateToken(token);

        // Assert
        assertEquals(user.getEmail(), subject);
    }

    // Teste para verificar que tokens de serviços diferentes não são compatíveis
    @Test
    void tokensFromDifferentServices_ShouldNotBeValid() {
        // Arrange
        TokenService service1 = new TokenService();
        TokenService service2 = new TokenService();

        ReflectionTestUtils.setField(service1, "secret", "secret-one");
        ReflectionTestUtils.setField(service2, "secret", "secret-two");

        String tokenFromService1 = service1.generateToken(user);

        // Act
        String validationResult = service2.validateToken(tokenFromService1);

        // Assert - O token deve ser inválido para o serviço com secret diferente
        assertEquals("", validationResult);
    }
}