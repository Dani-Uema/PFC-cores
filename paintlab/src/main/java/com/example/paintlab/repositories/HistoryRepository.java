package com.example.paintlab.repositories;

import com.example.paintlab.domain.history.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, UUID> {
}
