package com.example.paintlab.controller;

import com.example.paintlab.domain.user.User;
import com.example.paintlab.domain.user.UserRole;
import com.example.paintlab.dto.user.*;
import com.example.paintlab.repositories.UserRepository;
import com.example.paintlab.service.TokenService;
import com.example.paintlab.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        try {
            if(this.repository.findByEmail(data.email()) != null)
                return ResponseEntity.badRequest().build();

            String encryptedPassword = passwordEncoder.encode(data.password());
            User newUser = new User(data.name(), data.email(), encryptedPassword, data.role());
            this.repository.save(newUser);

            return ResponseEntity.ok("Usuário cadastrado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao cadastrar: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserDTO data) {
        try{
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            User user = (User) auth.getPrincipal();
            var token = tokenService.generateToken(user);
            String userRole = user.getRole().name();

            return ResponseEntity.ok(new LoginResponseDTO(
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    userRole
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body("Erro no login: " + e.getMessage());
        }
    }

    @PutMapping("/profile")
    public ResponseEntity updateProfile(@RequestBody UserUpdateDTO data,
                                        @AuthenticationPrincipal User authenticatedUser) {
        try {
            User updatedUser = userService.updateProfile(
                    authenticatedUser.getId(),
                    data.name(),
                    data.email()
            );

            return ResponseEntity.ok("Perfil atualizado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity updatePassword(@RequestBody PasswordUpdateDTO data,
                                         @AuthenticationPrincipal User authenticatedUser) {
        try {
            userService.updatePassword(
                    authenticatedUser.getId(),
                    data.currentPassword(),
                    data.newPassword()
            );

            return ResponseEntity.ok("Senha atualizada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/account")
    public ResponseEntity deleteAccount(@AuthenticationPrincipal User authenticatedUser) {
        try {
            userService.deleteAccount(authenticatedUser.getId());
            return ResponseEntity.ok("Conta deletada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao deletar conta: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenService.invalidateToken(token);
            return ResponseEntity.ok("Logout realizado com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou ausente.");
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity createAdmin(@RequestBody RegisterDTO data,
                                      @AuthenticationPrincipal User authenticatedUser) {
        try {
            if (!authenticatedUser.getRole().equals(UserRole.ADMIN)) {
                return ResponseEntity.status(403).body("Apenas administradores podem criar outros admins");
            }

            if (repository.existsByEmail(data.email())) {
                return ResponseEntity.badRequest().body("Email já está em uso");
            }

            String encryptedPassword = passwordEncoder.encode(data.password());
            User newAdmin = new User(data.name(), data.email(), encryptedPassword, UserRole.ADMIN);
            this.repository.save(newAdmin);

            return ResponseEntity.ok("Administrador criado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao criar administrador: " + e.getMessage());
        }
    }
}