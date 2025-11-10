package com.example.paintlab.service;

import com.example.paintlab.domain.user.User;
import com.example.paintlab.domain.history.History;
import com.example.paintlab.domain.ai.AIAnalysis;
import com.example.paintlab.repositories.UserRepository;
import com.example.paintlab.repositories.HistoryRepository;
import com.example.paintlab.repositories.AIAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User updateProfile(UUID userId, String name, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name.trim());
        }

        if (email != null && !email.trim().isEmpty() && !email.equals(user.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email já está em uso");
            }
            user.setEmail(email.trim());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Nova senha deve ter pelo menos 6 caracteres");
        }

        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(UUID userId) {
        System.out.println("🎯 INICIANDO EXCLUSÃO DA CONTA: " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        try {
            System.out.println("🗑️ Excluindo análises IA do usuário...");
            aiAnalysisRepository.deleteByUser(user);

            System.out.println("🗑️ Excluindo histórico de consultas...");
            historyRepository.deleteByUser(user);

            System.out.println("🗑️ Excluindo usuário...");
            userRepository.delete(user);

            System.out.println("✅ CONTA EXCLUÍDA COM SUCESSO: " + userId);

        } catch (Exception e) {
            System.out.println("❌ ERRO AO EXCLUIR CONTA: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir conta: " + e.getMessage());
        }
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}