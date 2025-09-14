package com.example.paintlab.repositories;

import com.example.paintlab.domain.pigments.Pigment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PigmentsRepository extends JpaRepository<Pigment, UUID> {
}
