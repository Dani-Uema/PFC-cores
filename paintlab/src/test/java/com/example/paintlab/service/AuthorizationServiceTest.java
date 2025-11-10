package com.example.paintlab.service;

import com.example.paintlab.domain.user.User;
import com.example.paintlab.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldReturnNull_WhenUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(email);

        // Assert
        assertNull(result); // ← COMPORTAMENTO REAL: retorna null
        verify(userRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldReturnNull_WhenEmailIsNull() {
        // Arrange
        when(userRepository.findByEmail(null)).thenReturn(null);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(null);

        // Assert
        assertNull(result); // ← COMPORTAMENTO REAL: retorna null
        verify(userRepository).findByEmail(null);
    }

    @Test
    void loadUserByUsername_ShouldReturnNull_WhenEmailIsEmpty() {
        // Arrange
        String emptyEmail = "";
        when(userRepository.findByEmail(emptyEmail)).thenReturn(null);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(emptyEmail);

        // Assert
        assertNull(result); // ← COMPORTAMENTO REAL: retorna null
        verify(userRepository).findByEmail(emptyEmail);
    }

    @Test
    void loadUserByUsername_ShouldUseEmailAsUsername() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(email);

        // Assert
        assertEquals(email, result.getUsername());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldReturnSameUserInstance() {
        // Arrange
        String email = "admin@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("adminPassword");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(email);

        // Assert
        assertSame(user, result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldCallRepositoryWithExactEmail() {
        // Arrange
        String email = "exact@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        authorizationService.loadUserByUsername(email);

        // Assert
        verify(userRepository).findByEmail(eq(email));
    }

    @Test
    void loadUserByUsername_ShouldHandleUserWithAuthorities() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getAuthorities());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void contextLoads() {
        assertNotNull(authorizationService);
        assertNotNull(userRepository);
    }
}