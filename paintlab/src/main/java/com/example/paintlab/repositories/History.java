package com.example.paintlab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface History extends JpaRepository<History, UUID> {
}
