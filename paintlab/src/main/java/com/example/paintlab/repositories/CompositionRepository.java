package com.example.paintlab.repositories;

import com.example.paintlab.domain.composition.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompositionRepository extends JpaRepository<Composition, UUID> {
}
