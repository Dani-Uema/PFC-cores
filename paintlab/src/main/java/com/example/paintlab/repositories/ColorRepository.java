package com.example.paintlab.repositories;

import com.example.paintlab.domain.color.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ColorRepository extends JpaRepository<Color, UUID> {
    Optional<Color> findByNameIgnoreCase(String name);
    boolean existsByName(String name);
}
