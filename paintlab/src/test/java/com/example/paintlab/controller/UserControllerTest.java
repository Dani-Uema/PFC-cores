package com.example.paintlab.controller;

import com.example.paintlab.domain.user.User;
import com.example.paintlab.domain.user.UserRole;
import com.example.paintlab.dto.user.*;
import com.example.paintlab.repositories.UserRepository;
import com.example.paintlab.service.TokenService;
import com.example.paintlab.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private RegisterDTO registerDTO;
    private UserDTO userDTO;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User("Dani", "dani@email.com", "encodedPassword", UserRole.USER);

        try {
            var idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, userId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao setar ID no usuário", e);
        }

        registerDTO = new RegisterDTO("John Doe", "john@email.com", "password123", UserRole.USER);
        userDTO = new UserDTO("john@email.com", "password123");
    }

    @Test
    void register_ShouldReturnOk_WhenUserIsValid() {
        // Arrange
        when(userRepository.findByEmail(registerDTO.email())).thenReturn(null);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity response = userController.register(registerDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário cadastrado com sucesso", response.getBody());
        verify(userRepository).findByEmail(registerDTO.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(registerDTO.email())).thenReturn(user);

        // Act
        ResponseEntity response = userController.register(registerDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_ShouldReturnOkWithToken_WhenCredentialsAreValid() {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("jwt-token");

        ResponseEntity response = userController.login(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof LoginResponseDTO);

        LoginResponseDTO loginResponse = (LoginResponseDTO) response.getBody();
        assertEquals("jwt-token", loginResponse.token());
        assertEquals("John Doe", loginResponse.name());
        assertEquals("john@email.com", loginResponse.email());
        assertEquals("USER", loginResponse.role());
    }

    @Test
    void login_ShouldReturnForbidden_WhenCredentialsAreInvalid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity response = userController.login(userDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro no login"));
    }

    @Test
    void updateProfile_ShouldReturnOk_WhenUpdateIsSuccessful() {
        UserUpdateDTO updateDTO = new UserUpdateDTO("New Name", "new@email.com");
        when(userService.updateProfile(eq(userId), eq("New Name"), eq("new@email.com")))
                .thenReturn(user);

        ResponseEntity response = userController.updateProfile(updateDTO, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Perfil atualizado com sucesso", response.getBody());
        verify(userService).updateProfile(userId, "New Name", "new@email.com");
    }

    @Test
    void updatePassword_ShouldReturnOk_WhenPasswordIsUpdated() {
        // Arrange
        PasswordUpdateDTO passwordDTO = new PasswordUpdateDTO("currentPass", "newPass");
        doNothing().when(userService).updatePassword(eq(userId), eq("currentPass"), eq("newPass"));

        // Act
        ResponseEntity response = userController.updatePassword(passwordDTO, user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Senha atualizada com sucesso", response.getBody());
        verify(userService).updatePassword(userId, "currentPass", "newPass");
    }

    @Test
    void deleteAccount_ShouldReturnOk_WhenAccountIsDeleted() {
        // Arrange
        doNothing().when(userService).deleteAccount(userId);

        // Act
        ResponseEntity response = userController.deleteAccount(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Conta deletada com sucesso", response.getBody());
        verify(userService).deleteAccount(userId);
    }

    @Test
    void logout_ShouldReturnOk_WhenTokenIsValid() {
        // Arrange
        String authHeader = "Bearer valid-token";
        doNothing().when(tokenService).invalidateToken("valid-token");

        // Act
        ResponseEntity response = userController.logout(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logout realizado com sucesso.", response.getBody());
        verify(tokenService).invalidateToken("valid-token");
    }

    @Test
    void createAdmin_ShouldReturnOk_WhenAdminCreatesNewAdmin() {
        // Arrange
        User adminUser = new User("Admin User", "admin@email.com", "password", UserRole.ADMIN);
        try {
            var idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(adminUser, UUID.randomUUID());
        } catch (Exception e) {
            fail("Erro ao configurar admin user");
        }

        when(userRepository.existsByEmail(registerDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity response = userController.createAdmin(registerDTO, adminUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Administrador criado com sucesso", response.getBody());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createAdmin_ShouldReturnForbidden_WhenUserIsNotAdmin() {
        // Arrange (user is regular USER, not ADMIN)

        // Act
        ResponseEntity response = userController.createAdmin(registerDTO, user);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Apenas administradores podem criar outros admins", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }

    // Teste simples para verificar se tudo está funcionando
    @Test
    void contextLoads() {
        assertNotNull(userController);
        assertNotNull(userRepository);
        assertNotNull(tokenService);
    }
}