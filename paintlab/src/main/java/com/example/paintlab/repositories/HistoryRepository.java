package com.example.paintlab.repositories;

import com.example.paintlab.domain.history.History;
import com.example.paintlab.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, UUID> {
    @EntityGraph(attributePaths = {"color", "user"})
    List<History> findByUserId(UUID userId);
    void deleteByUser(User user);

}
