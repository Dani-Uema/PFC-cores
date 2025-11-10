package com.example.paintlab.service;

import com.example.paintlab.domain.user.User;
import com.example.paintlab.domain.history.History;
import com.example.paintlab.domain.ai.AIAnalysis;
import com.example.paintlab.repositories.UserRepository;
import com.example.paintlab.repositories.HistoryRepository;
import com.example.paintlab.repositories.AIAnalysisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private AIAnalysisRepository aiAnalysisRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void updateProfile_ShouldUpdateName_WhenValidNameProvided() {
        // Arrange
        String newName = "John Updated";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateProfile(userId, newName, null);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(argThat(u ->
                u.getName().equals("John Updated") &&
                        u.getEmail().equals("john@example.com") // Email mantido
        ));
    }

    @Test
    void updateProfile_ShouldUpdateEmail_WhenValidEmailProvided() {
        // Arrange
        String newEmail = "newemail@example.com";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateProfile(userId, null, newEmail);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository).save(argThat(u ->
                u.getEmail().equals("newemail@example.com") &&
                        u.getName().equals("John Doe") // Nome mantido
        ));
    }

    @Test
    void updateProfile_ShouldUpdateBothNameAndEmail_WhenBothProvided() {
        // Arrange
        String newName = "John Updated";
        String newEmail = "newemail@example.com";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateProfile(userId, newName, newEmail);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository).save(argThat(u ->
                u.getName().equals("John Updated") &&
                        u.getEmail().equals("newemail@example.com")
        ));
    }

    @Test
    void updateProfile_ShouldNotUpdateEmail_WhenEmailIsSame() {
        // Arrange
        String sameEmail = "john@example.com"; // Mesmo email atual
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateProfile(userId, null, sameEmail);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository, never()).existsByEmail(anyString()); // Não verifica existência do mesmo email
        verify(userRepository).save(argThat(u ->
                u.getEmail().equals("john@example.com") // Email mantido
        ));
    }

    @Test
    void updateProfile_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        String existingEmail = "existing@example.com";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateProfile(userId, null, existingEmail);
        });

        assertTrue(exception.getMessage().contains("Email já está em uso"));
        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail(existingEmail);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateProfile_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateProfile(userId, "New Name", null);
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateProfile_ShouldTrimNameAndEmail() {
        // Arrange
        String nameWithSpaces = "  John Updated  ";
        String emailWithSpaces = "  newemail@example.com  ";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateProfile(userId, nameWithSpaces, emailWithSpaces);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(argThat(u ->
                u.getName().equals("John Updated") && // Sem espaços
                        u.getEmail().equals("newemail@example.com") // Sem espaços
        ));
    }

    @Test
    void updateProfile_ShouldIgnoreNullValues() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateProfile(userId, null, null);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(argThat(u ->
                u.getName().equals("John Doe") && // Nome original mantido
                        u.getEmail().equals("john@example.com") // Email original mantido
        ));
    }

    @Test
    void updatePassword_ShouldUpdatePassword_WhenValidCurrentPassword() {
        // Arrange
        String currentPassword = "currentPassword";
        String newPassword = "newPassword123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.updatePassword(userId, currentPassword, newPassword);

        // Assert
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches(currentPassword, user.getPassword());
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(argThat(u ->
                u.getPassword().equals("encodedNewPassword")
        ));
    }

    @Test
    void updatePassword_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(userId, "current", "new");
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository).findById(userId);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePassword_ShouldThrowException_WhenCurrentPasswordIncorrect() {
        // Arrange
        String wrongPassword = "wrongPassword";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(userId, wrongPassword, "newPassword");
        });

        assertTrue(exception.getMessage().contains("Senha atual incorreta"));
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches(wrongPassword, user.getPassword());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePassword_ShouldThrowException_WhenNewPasswordTooShort() {
        // Arrange
        String currentPassword = "currentPassword";
        String shortPassword = "12345"; // Menos de 6 caracteres

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(userId, currentPassword, shortPassword);
        });

        assertTrue(exception.getMessage().contains("Nova senha deve ter pelo menos 6 caracteres"));
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches(currentPassword, user.getPassword());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePassword_ShouldThrowException_WhenNewPasswordIsNull() {
        // Arrange
        String currentPassword = "currentPassword";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(userId, currentPassword, null);
        });

        assertTrue(exception.getMessage().contains("Nova senha deve ter pelo menos 6 caracteres"));
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches(currentPassword, user.getPassword());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteAccount_ShouldDeleteUserAndRelatedData() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(aiAnalysisRepository).deleteByUser(user);
        doNothing().when(historyRepository).deleteByUser(user);
        doNothing().when(userRepository).delete(user);

        // Act
        userService.deleteAccount(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository).deleteByUser(user);
        verify(historyRepository).deleteByUser(user);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteAccount_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteAccount(userId);
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository, never()).deleteByUser(any());
        verify(historyRepository, never()).deleteByUser(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteAccount_ShouldHandleRepositoryExceptions() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Database error")).when(aiAnalysisRepository).deleteByUser(user);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteAccount(userId);
        });

        assertTrue(exception.getMessage().contains("Erro ao excluir conta"));
        verify(userRepository).findById(userId);
        verify(aiAnalysisRepository).deleteByUser(user);
        verify(historyRepository, never()).deleteByUser(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John Doe", result.getName());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findById(userId);
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(userRepository).findById(userId);
    }

    @Test
    void contextLoads() {
        assertNotNull(userService);
        assertNotNull(userRepository);
        assertNotNull(historyRepository);
        assertNotNull(aiAnalysisRepository);
        assertNotNull(passwordEncoder);
    }
}
