package com.example.paintlab.repositories;

import com.example.paintlab.domain.color.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColorRepository extends JpaRepository<Color, UUID> {

    Optional<Color> findByNameIgnoreCase(String name);
    boolean existsByName(String name);
    Color findByColorCode(String colorCode);


    List<Color> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrColorCodeContainingIgnoreCase(
            String name, String brand, String colorCode
    );
}