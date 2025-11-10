package com.example.paintlab.repositories;

import com.example.paintlab.domain.ai.AIAnalysis;
import com.example.paintlab.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AIAnalysisRepository extends JpaRepository<AIAnalysis, UUID> {
    List<AIAnalysis> findByUserId(UUID userId);
    List<AIAnalysis> findByUserIdOrderByAnalysisDateDesc(UUID userId);
    void deleteByUser(User user);

}
