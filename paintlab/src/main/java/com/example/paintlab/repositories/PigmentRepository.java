package com.example.paintlab.repositories;

import com.example.paintlab.domain.pigments.Pigment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PigmentRepository extends JpaRepository<Pigment, UUID> {
    Optional<Pigment> findByName(String name);
    Optional<Pigment> findByNameIgnoreCase(String name);
}
